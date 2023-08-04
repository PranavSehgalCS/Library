/*///////////////////////////////////////////////////////////////////////////////////////////////////////
*   FILE  : borroe.component.ts
*   AUTHOR : Pranav Sehgal
*   DESCRIPTION : USED as the typescript file for Viewing Borrows
///////////////////////////////////////////////////////////////////////////////////////////////////////*/
import { Tag } from 'src/app/model/Tag';
import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { Borrow } from 'src/app/model/Borrow';
import { Title } from '@angular/platform-browser';
import { BorrowService } from 'src/app/services/borrow.service';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-borrow',
  templateUrl: './borrow.component.html',
  styleUrls: ['borrow.css']
})

export class BorrowComponent{
  public tagArr:Tag[] = [];
  public borrArr:Borrow[] = [];
  public isAdmin:boolean = false;
  constructor(private title:Title,
              private router:Router,
              private bService:BorrowService,
              private accService:AccountService){
    if(!accService.isLoggedIn()){
      router.navigate(['/login']);
    }else if(accService.isAdmin()){
      this.isAdmin = true;
      this.title.setTitle("View Customers");
    }else{
      this.title.setTitle("View Borrows");
    }
  }

  ngOnInit(){
    if(this.accService.isAdmin()){
      this.bService.getBorrows().subscribe( res => {
        this.borrArr = [];
        if( res!= undefined && res!=null){
          this.tagArr = res;
        }else{
          alert("ERROR While Getting Borrows");
        }
      })
    }else{
      this.bService.getMyBorrows(this.accService.getAccName()).subscribe( res =>{
        this.tagArr = [];
        if(res!=undefined && res!=null){
          this.borrArr = res;
        }else{
          alert("ERROR While Getting Your Borrows");
        }
      });
    }
  }

  async viewBooks(accName:string){
    this.bService.getMyBorrows(accName).subscribe( res => {
      if(res!=null && res!=undefined){
        var count:number = 0;
        var alVal:string ="\nThe following books were borrowed by "+accName+": \n";
        for(let bb of res){
          count++;
          alVal += '\n' + count + ':  ' + bb.bookName;
        }
        alert(alVal);
      }else{
        alert("ERROR While getting books");
      }
    })
  }
  async retBook(bookId:number){
    var accName:string = this.accService.getAccName();
    this.bService.deleteBorrow(accName, bookId).subscribe( res => {
      if(res==true){
        alert("Book returned successfully!");
      }else{
        alert("ERROR While returning book");
      }
      location.reload();
    });
  }


  
}
