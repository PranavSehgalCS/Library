import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Account } from '../model/Account';
import { catchError, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private accURL:string = 'http://localhost:8080/accounts';
  constructor(private http: HttpClient) { }

  hashPass(pwd:string): string{
    var shift:number = pwd.length;
    var retVal:string = "";
    for (let i = 0; i<pwd.length; i++) {
      retVal =  String.fromCharCode(pwd.charCodeAt(i)+shift/2) 
                + retVal +
                String.fromCharCode(pwd.charCodeAt(i)+shift);
      shift+=(i+pwd.charCodeAt(i)/3);
    }
    for (let i = 0; i<retVal.length; i++) {
      if(retVal.charAt(i) == "'"){
        retVal = retVal.substring(0,i)+"%"+retVal.substring(i,retVal.length);
      }
    }
    return ("#0xp"+retVal)+"@pass";
  }

  delay(ms: number){
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  getAccount(accName:string, accPass:string): Observable<Account> {
    let Params = new HttpParams().append("accName",String(accName))
                                  .append("accPass", this.hashPass(accPass));
    return this.http.request<Account>("GET",this.accURL,{responseType:"json", params:Params}); 
  }

  addAccount(account: Account): Observable<number> {
    let Params = new  HttpParams().append("accName",String(account.accName))
                                  .append("accPass", this.hashPass(account.accPass)); 
    return this.http.request<number>("POST",this.accURL,{responseType:"json", params:Params});
  }

  updateAccount(account: Account){
    let Params = new  HttpParams().append("accName",String(account.accName))
                                  .append("accPass", account.accPass); 
    return this.http.request<Account>("PUT",this.accURL,{responseType:"json", params:Params});
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
