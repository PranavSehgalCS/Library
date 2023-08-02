/*///////////////////////////////////////////////////////////////////////////////////////////////////////
*   FILE  : view-book.component.ts
*   AUTHOR : Pranav Sehgal
*   DESCRIPTION : USED as the typescript file for writing functions for viewing book
///////////////////////////////////////////////////////////////////////////////////////////////////////*/

import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { Book } from 'src/app/model/Book';
import { Title } from '@angular/platform-browser';
import { BookService } from 'src/app/services/book.service';
import { AccountService } from 'src/app/services/account.service';
import { min } from 'rxjs';
@Component({
  selector: 'app-view-book',
  templateUrl: './view-book.component.html',
  styleUrls: ['../bookStyles.css']
})
export class ViewBookComponent {
  public reload:boolean = true;
  public isAdmin:boolean = false;
  public tagArray:string[]= [];
  public bookArray:Book[] = [];
  
  constructor(
    public router:Router,
    private title:Title,
    private bookService:BookService,
    private accService:AccountService
  ){
    if(!accService.isLoggedIn()){
      router.navigate(['login']);
    }else{
      this.title.setTitle('Avalible Books')
      this.isAdmin = accService.isAdmin();
    }
  }

  ngOnInit(){
    this.bookService.getBooks(-1).subscribe(res => {
      if(res!= null && res!= undefined){
        this.bookArray = res;
        for(let book of this.bookArray){
          for(let tagg of book.bookTags){
            if(!this.tagArray.includes(tagg)){
              this.tagArray.push(tagg);
            }
          }
        }
        this.reload = true;
      }else{
        alert("ERROR while getting books");
      }});
  }

  async clear(){
    this.bookService.getBooks(-1).subscribe(res => {
      if(res!= null && res!= undefined){
        this.bookArray = res;
        this.reload = true;
      }else{
        alert("ERROR while getting books");
      }});
  }
  async filter(bookAuth:string, minAge:number, maxAge:number, tag:string){
    if(bookAuth.length==0){
      bookAuth='null';
    }
    if(bookAuth=='null' && minAge == 0 && maxAge == 100 && tag=='null'){
      alert('No Filter Set!')
    }else{
      this.bookService.getByFilter( bookAuth, [minAge,maxAge], [tag]).subscribe(res =>{
        if(res!=null && res!=undefined){
          if(res[0].bookId!=-1){
            this.bookArray = res;
            this.reload =true;
          }else{
            this.bookArray=[];
            alert("No books found with set filters");
          }
        }else{
          this.bookArray=[];
        }
      });
    }
  }
  
  async deleteBooks(book:Book){
    if(confirm("Are you sure you want to delete the book :\n\n'"+book.bookName+"'")){
      this.bookService.deleteBook(book.bookId).subscribe( res => {
        if(res==true){
          if(this.bookArray.length!=0){
            if(this.bookArray.length==1){
              this.bookArray = [];
            }else{
              var temp:Book[] = [];
              for(let b of this.bookArray){
                if(b.bookId!=book.bookId){
                  temp.push(b);
                }
              }
              this.bookArray = temp;
            }
          }

          alert('Book deleted successfully!')
        }else{
          alert('ERROR while deleting book')
        }
      });
    }
  }
}
