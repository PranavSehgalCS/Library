///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : Templates.ts
//  AUTHOR : Pranav Sehgal
//  DESCRIPTION: Is a template ts service with constructor to encapsulate data 
//               USE this as a template to create your own ts service that can communicate with backend
//               TO CREATE new service, use : ng generate service <service name>
///////////////////////////////////////////////////////////////////////////////////////////////////////
import { Tag } from '../model/Tag';
import { Observable, of } from 'rxjs';
import { Borrow } from '../model/Borrow';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
///////////////////////////////////////////////////////////////////////////////////////////////////////

@Injectable({
  providedIn: 'root'
})
export class BorrowService {
  public errString:string = "";
  private borrowUrl:string = 'http://localhost:8080/borrows';

  constructor(private http: HttpClient) { }

  getBorrows(): Observable<Tag[]>{
    return this.http.get<Tag[]>(this.borrowUrl);
  }

  getMyBorrows(accName:string): Observable<Borrow[]>{
    return this.http.get<Borrow[]>(this.borrowUrl+'/'+accName);
  }

  createBorrow(accName:string, borrDate:string, bookId:number, bookName:string): Observable<boolean>{
    const param = new HttpParams().append("accName", accName)
                                  .append("borrDate", borrDate)
                                  .append("bookId", bookId)
                                  .append("bookName", bookName);
    return this.http.post<boolean>(this. borrowUrl,null,{params:param});
  }

  deleteBorrow(accName:string,bookId:number): Observable<boolean>{
    const param = new HttpParams().append("bookId", bookId)
                                  .append("accName", accName);
    return this.http.delete<boolean>(this.borrowUrl,{params:param});
  }


}
