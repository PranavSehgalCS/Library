import { Router } from '@angular/router';
import { Component } from '@angular/core';
import {Title} from '@angular/platform-browser';
import { Account } from 'src/app/model/Account';
import { AccountService } from 'src/app/services/account.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../Account.css']
})
export class LoginComponent {
  public errMsg:String ="";
  public gotAccount: Account = new Account(-1,'','',false);
  constructor(
    private router:Router,
    private titleService:Title,
    private accountService: AccountService
  ){
    if(accountService.isLoggedIn()){
      this.router.navigate(['/dashboard']);
    }else{
      this.titleService.setTitle('Login to Library'); 
    }
  } 
  async submitLoginForm(accName:string, accPass:string){
    accName = accName.toLowerCase().trim();
    accPass = this.accountService.hashPass(accPass.trim());
    
    if(accName.length == 0){
      this.errMsg = "No Username has been entered !!"
    }else if(accPass.length == 0){
      this.errMsg = ("Please enter a Password"); 
    }else if(accPass.length < 4 ){
      this.errMsg = ("Password must have at least 4 Characters"); 
    }else if(accPass.length > 32 ){
      this.errMsg = ("Password Length Can't Exceed 32 Characters"); 
    }else{
      await this.accountService.getAccount( accName, accPass).subscribe(res =>{
        this.gotAccount = res;
        if(this.gotAccount == null){
          alert("ERROR getting account From Backend");
        }
      });
      for(var i = 0; i<60;i++){
        if(this.gotAccount?.accId == -1){
          await this.accountService.delay(25);
        }else{
          break;
        }
      }
      if(this.gotAccount==undefined){
        this.errMsg = "Request Timed Out! Please Try Again!"
      }else if(this.gotAccount.accId == 0){
        this.errMsg = "Account Not Found! \nPlease Create One!"
      }else if(this.gotAccount.accId == 1){
        this.errMsg = "Incorrect Username or PassCode!";
      }else if(this.gotAccount.accId == 2){
        this.errMsg = "No Connection To Database! \nPlease Try Again Later"; 
      }else if(this.gotAccount.accId >4){
        this.gotAccount.accPass = accPass;
        this.accountService.setAccount(this.gotAccount);
        window.alert("Login Success! \n Welcome "+accName.trim().charAt(0).toUpperCase() +  accName.trim().slice(1).toLowerCase());
        this.router.navigate(['/dashboard']);
      }
    }
  }
}
