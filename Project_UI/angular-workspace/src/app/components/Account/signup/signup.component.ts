import { Router } from '@angular/router';
import { Component } from '@angular/core';
import {Title} from "@angular/platform-browser";
import { AccountService } from 'src/app/services/account.service';
import { Account } from 'src/app/model/Account';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['../Account.css']
})
export class SignupComponent {
  constructor(
    private router:Router, 
    private titleService:Title,
    private accountService:AccountService
  ){
    if(accountService.isLoggedIn()){
      this.router.navigate(['/dashboard']);
    }else{
      this.titleService.setTitle('Sign Up For Library!'); 
    }
  }
  
  public errMsg:string ="";

  async submitSignupForm( accName:string, accPass:string, accPass2:string){
    accName = accName.toLowerCase().trim();
    if(accName.length == 0) {
      this.errMsg = ("Please enter a Username");
    }else if(accPass.length == 0){
      this.errMsg = ("Please enter a Password");
    }else if(accPass!=accPass2){
      this.errMsg = ("The Passwords dont match!");
    }else if(accPass.length > 32){
      this.errMsg = ("Password Lenght Can't Exceed 32 Chars!");
    }else{
      try{
        const newUser = new Account( 0 , accName ,this.accountService.hashPass(accPass) , false );
        var acctResponse:number = -1;
        this.accountService.addAccount(newUser).subscribe( res =>{
          acctResponse=res;
        });
        for(var i = 0; i<30;i++){
          if(acctResponse == -1){
            await this.accountService.delay(100);
          }else{
            break;
          }
        }
 
        if( acctResponse == null){
          this.errMsg = ("Error occurred while adding account.");
        }else if( acctResponse == -1){
          this.errMsg = ("Request Timed Out");
        }else if( acctResponse == 1){
          this.errMsg = ("UserName Already Exists, Please Choose a New One");
        }else if( acctResponse == 2){
          this.errMsg = ("No DB Connection! Please try again later");
        }else if( acctResponse == 3){
          this.errMsg = ("Account ID Error(DB_Related)! Please try again later");
        }else if( acctResponse == 4){
          this.errMsg = ("A Backend Error Occured! Please try again later");
        }else if( acctResponse  == 0){
          alert("Account Created Successfully!\n Please continue to login!");
          this.router.navigate(['/login']);
        }else{
          alert(acctResponse);
          this.errMsg = ("Click Again To Confirm Creation...");}
      }catch(error){
        console.log(error);
        this.errMsg = ("Error occurred while adding account.");
      }
    }
    }
}

