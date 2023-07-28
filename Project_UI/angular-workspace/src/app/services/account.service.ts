import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Account } from '../model/Account';
import { catchError, tap } from 'rxjs/operators';
import { setCookie, getCookie, removeCookie } from 'typescript-cookie';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private currAccount: Account = new Account(0,"","",false);
  private accURL:string = 'http://localhost:8080/accounts';
  constructor(  private http: HttpClient) { }

  isLoggedIn():boolean{
    this.currAccount.accId = Number(getCookie('accId'));
    if(this.currAccount.accId>4){
      this.currAccount.accName = String(getCookie('accName'));
      this.currAccount.accPass = String(getCookie('accPass'));
      if(this.currAccount.accName == "" || this.currAccount.accPass == ""){
        return false;
      }
      this.currAccount.isAdmin = Boolean(getCookie('isAdmin'));

      return true;
    }else{
      return false;
    }
  }
  isAdmin():boolean{
    return (getCookie('isAdmin')=='true');
  }
  
  getLoggedAccount(){
    return this.currAccount;
  }
  setAccount(acc:Account):void{
    this.currAccount = acc;
    var properties = {expires: 0.2, path:'/'};
    setCookie('accId', String(acc.accId), properties);
    setCookie('accName', acc.accName, properties);
    setCookie('accPass', acc.accPass, properties);
    setCookie('isAdmin', acc.isAdmin, properties);
  }

  logout():void{
    this.currAccount = new Account(0,'','',false);
    removeCookie('accId');
    removeCookie('accName');
    removeCookie('accPass');
    setCookie('isAdmin', 'false');
  }

  hashPass(pwd:string): string{
    var retVal:string = "#0xp";
    var shift:number = 0;
    var addC:string= ' ';
    for (let i = 0; i<pwd.length; i++){
      addC = String.fromCharCode(pwd.charCodeAt(i)+shift);
      if(addC == "'" || addC=='"'){
        addC = 'X';
      }
      retVal = retVal + addC;
      shift++;
    }
    return (retVal+"@pass");
  }

  delay(ms: number){
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  getAccount(accName:string, accPass:string): Observable<Account> {
    let Params = new HttpParams().append("accName",String(accName))
                                  .append("accPass", accPass);
    return this.http.request<Account>("GET",this.accURL,{responseType:"json", params:Params}); 
  }

  addAccount(account: Account): Observable<number> {
    let Params = new  HttpParams().append("accName",account.accName)
                                  .append("accPass", account.accPass); 
    return this.http.request<number>("POST",this.accURL,{responseType:"json", params:Params});
  }

  updateAccount(account: Account):Observable<Boolean>{
    let Params = new  HttpParams().append("accId",String(account.accId))
                                  .append("accName",account.accName)
                                  .append("accPass", account.accPass); 
    return this.http.request<Boolean>("PUT",this.accURL,{responseType:"json", params:Params});
  }

  deleteAccount(acc:Account): boolean{
    const param = new HttpParams().append("accId",acc.accId)
                                  .append("accPass",acc.accPass)
                                  .append("accName",acc.accName);
    var retVal = this.http.delete(this.accURL,{params:param}).subscribe((s) => {
      console.log(s);
    });
    return Boolean(retVal)
  }

}
