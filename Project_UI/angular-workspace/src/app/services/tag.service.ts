///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : Templates.ts
//  AUTHOR : Pranav Sehgal
//           +Auto-generated on ng generate service 
//  DESCRIPTION: Is a service with constructor to encapsulate data and communicate with backend
///////////////////////////////////////////////////////////////////////////////////////////////////////

import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Tag } from '../model/Tag';
import { HttpClient, HttpParams } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class TagService {
  private tagUrl:string = 'http://localhost:8080/tags';

  constructor(private http: HttpClient) { }

  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }
  
  getTags(tagId:number): Observable<Tag[]>{
    return this.http.get<Tag[]>(this.tagUrl+"/"+String(tagId));
  }

  createTag(tagName:string): Observable<boolean>{
    const param = new HttpParams().append("tagName", tagName)
    return this.http.post<boolean>(this. tagUrl,null,{params:param});
  }

  updateTemplate(tagId:number, tagName:string): Observable<boolean>{
    const param = new HttpParams().append("tagId", tagId)
                                  .append("tagName", tagName);
    return this.http.put<boolean>(this.tagUrl,null,{params:param});
  }

  deleteTemplate(tagId:number): Observable<boolean>{
    const param = new HttpParams().append("tagId", tagId)
    return this.http.delete<boolean>(this.tagUrl,{params:param});
  }
}
