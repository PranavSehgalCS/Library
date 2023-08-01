import { Tag } from 'src/app/model/Tag';
import { Book } from 'src/app/model/Book';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TagService } from 'src/app/services/tag.service';
import { BookService } from 'src/app/services/book.service';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-edit-book',
  templateUrl: './edit-book.component.html',
  styleUrls: ['../bookStyles.css']
})
export class EditBookComponent {
  private canEdit:boolean = false;
  public tagArr:Tag[] = [];
  public chosenArr:string[] = [];
  public reload:boolean = true;

  constructor(private router:Router,
              private tagService:TagService,
              private route: ActivatedRoute,
              private bookService:BookService,
              private accService:AccountService){
    if(!accService.isLoggedIn()){
      router.navigate(['/login']);
    }else if(!accService.isAdmin()){
      router.navigate(['/dashboard']);
    }
  }

  public bookId:number =-1;
  public minVal:number = 0;
  public maxVal:number = 0;
  public bookName:string = " ";
  public bookAuth:string = " ";

  ngOnInit(){
    this.bookId = Number(this.route.snapshot.queryParamMap.get('bookId'));
    if(this.bookId<=0){
      alert("No Id Provided!");
    }else{
      this.bookService.getBooks(this.bookId).subscribe( resVal => {
        var curVal= resVal.pop();
        if(curVal?.bookId ==undefined){
          alert("Invalid Id Provided \n or ERROR While getting book!");
        }else{
          this.tagService.getTags(-1).subscribe( res => {
            if(res==null || res == undefined){
              alert("ERROR While Getting Tags!");
            }else{
              this.tagArr = res;
            }
          });
          this.minVal = curVal.ageRange[0];
          this.maxVal = curVal.ageRange[1];
          this.bookName = curVal.bookName;
          this.bookAuth = curVal.bookAuth;
          this.chosenArr = curVal.bookTags;
        }
      });
      this.canEdit = true;
    }
  }
  toNum(inVal:string):number{
    var retVal:number = Number(inVal);
    if(retVal<0){
      retVal=0;
    }else if(retVal>100){
      retVal = 100;
    }
    return retVal;
  }
  addTag(name:string){
      this.chosenArr.push(name);
  }
  
  removeTag(name:string){
    var remInd:number = this.chosenArr.indexOf(name);
    var temp = [];
    for(let tagName of this.chosenArr){
      if(tagName!=name){
        temp.push(tagName);
      }
    }
    this.chosenArr = temp;
  }

  editBook(bookName:string, bookAuth:string, minAge:number, maxAge:number){
    bookName = bookName.trim();
    bookAuth = bookAuth.trim();
    if(bookName.length<3){
      alert("Book name must be at least 3 characters");
    }else if(bookAuth.length<2){
      alert("Author name must be at least 3 characters");
    }else if(minAge>=maxAge){
      alert("Minimum Age must be less than Maximum Age");
    }else{
      if(!this.canEdit){
        alert("The Following Book Can't be Edited");
      }else{
        var upBook:Book = new Book(this.bookId, [minAge,maxAge],bookName,bookAuth,this.chosenArr);
        this.bookService.updateBook(upBook).subscribe( res => {
          if(res == true){
            alert("Book updated Successfully!");
          }else{
            alert("ERROR While updating book!");
          }
        });
      }
    }
  }
}
