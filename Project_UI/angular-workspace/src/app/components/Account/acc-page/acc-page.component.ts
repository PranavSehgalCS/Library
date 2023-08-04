import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Account } from 'src/app/model/Account';
import { AccountService } from 'src/app/services/account.service';
@Component({
  selector: 'app-acc-page',
  templateUrl: './acc-page.component.html',
  styleUrls: ['../Account.css']
})
export class AccPageComponent {
  errMsg:string = "";
  public curAcc:Account = new Account(0,"","",false);
  constructor(private accService:AccountService,
              private title:Title,
              private router:Router){
    if(!accService.isLoggedIn()){
      this.router.navigate(['/login']);
    }else{
      this.title.setTitle("Account Page");
      this.curAcc = this.accService.getLoggedAccount();
    }

  }
  async logout(i:boolean){
    if(!i){
      this.accService.logout();
      this.router.navigate(['/login']);
    }else if(confirm("Are You Sure You Want To Log-Out?")){
      this.accService.logout();
      this.router.navigate(['/login']);
    }

  }
  async submitUpdatePass(accPass:string, accPass2:string){
    if(accPass.length==0){
      this.errMsg="Please Enter A PassWord First!"
    }else if(accPass!=accPass2){
      this.errMsg="Your Passwords Don't Match";
    }else if(this.accService.hashPass(accPass)==this.curAcc.accPass){
      this.errMsg = "New Password Can't Be Same As Old One!";
    }else if(!confirm("Are You Sure You Want To Change Your Password to '"+accPass+"'")){
      this.errMsg="" 
    }else{
      this.errMsg = "";
      var upAcc = new Account(this.curAcc.accId, this.curAcc.accName, this.accService.hashPass(accPass),false);
      await this.accService.updateAccount(upAcc).subscribe(res=>{
        if(res==true){
          this.accService.setAccount(upAcc);
          this.curAcc = upAcc;
          alert("Password Successfully Changed To: " + accPass);
        }else{
          alert("ERROR While Changing Password!");
        }
      });
    }
  }
  
  async submitUpdateName(accName:string){
    accName = accName.toLowerCase().trim();
    if(accName.length==0){
      this.errMsg="Please Enter A Name";
    }else if(accName == this.curAcc.accName){
      this.errMsg="New Name Can't Be Old Name";
    }else{
      this.errMsg="";
      var upAcc = new Account(this.curAcc.accId, accName,this.curAcc.accPass,false);
      await this.accService.updateAccount(upAcc).subscribe(res=>{
        if(res==true){
          this.accService.setAccount(upAcc);
          alert("UserName Successfully Changed To: "+ accName);
        }else if(res == false){
          alert("Error While Changing UserName!");
        }else{
          alert(res);
        }
      });
    }
    
  }
  async submitDelForm(){
    if(confirm("Are You Sure You Want To Delete Account")){
      if(this.accService.deleteAccount(this.curAcc)){
        alert("Account Delete Successfully, Logging You Out!");
        this.logout(false);
      }
    }
  }
}
