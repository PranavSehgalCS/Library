///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : Borrow.ts
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a model with constructor to encapsulate borrow data 
///////////////////////////////////////////////////////////////////////////////////////////////////////
export class Borrow{
    public accName:string = "";
    public borrDate:string = "";
    public bookId:number = -1;
    public bookName:string = "";

    public constructor(accName:string, borrDate:string, bookId:number, bookName:string) {
        this.accName = accName;
        this.borrDate = borrDate;
        this.bookId = bookId;
        this.bookName = bookName;
    }
}