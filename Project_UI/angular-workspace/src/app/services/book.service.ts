///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : Books.ts
//  AUTHOR : Pranav Sehgal
//           +Auto-generated on ng generate service 
//  DESCRIPTION: Is a template ts service with constructor to encapsulate data 
//               USE this as a template to create your own ts service that can communicate with backend
//               TO CREATE new service, use : ng generate service <service name>
///////////////////////////////////////////////////////////////////////////////////////////////////////
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Book } from '../model/Book';
import { HttpClient, HttpParams } from '@angular/common/http';
///////////////////////////////////////////////////////////////////////////////////////////////////////

@Injectable({
  providedIn: 'root'
})
export class BookService {
  
  private bookUrl:string = 'http://localhost:8080/books';
  constructor(private http: HttpClient){
  }
  
  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }
  
  getBooks(bookId:number): Observable<Book[]>{
    const param = new HttpParams().append("bookId", bookId);
    return this.http.get<Book[]>(this.bookUrl, {params:param});
  }

  getByFilter(bookAuth:string, ageRange:number[], bookTags:string[]):Observable<Book[]>{
    try {
      const param = new HttpParams().append("bookAuth", bookAuth)
                                    .append("ageRange", String(ageRange))
                                    .append("bookTags", String(bookTags));
      var book =  this.http.get<Book[]>(this.bookUrl+'/filter', {params:param});
      if(book == null || book == undefined){
        alert("YELL");
      }
      return book;

    }catch (error) {
      alert("NOPES");
      var book:Observable<Book[]> = new Observable<Book[]>;
      return book;
    }

  }


  createBook(newBook:Book): Observable<boolean>{
    const param = new HttpParams().append("bookName", newBook.bookName)
                                  .append("bookAuth", newBook.bookAuth)
                                  .append("ageRange", String(newBook.ageRange))
                                  .append("bookTags", String(newBook.bookTags));
    return this.http.post<boolean>(this.bookUrl,null,{params:param});
  }

  updateBook(upBook:Book): Observable<boolean>{
    const param = new HttpParams().append("bookName", upBook.bookName)
                                  .append("bookAuth", upBook.bookAuth)
                                  .append("ageRange", String(upBook.ageRange))
                                  .append("bookTags", String(upBook.bookTags));
    return this.http.put<boolean>(this.bookUrl,null,{params:param});
  }

  deleteBook(bookId:number): Observable<boolean>{
    const param = new HttpParams().append("bookId",bookId)
    return this.http.delete<boolean>(this.bookUrl,{params:param});
  }

}
