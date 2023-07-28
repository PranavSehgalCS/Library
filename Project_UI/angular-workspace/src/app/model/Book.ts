///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : Book.ts
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a template ts Book model with constructor to encapsulate data 
///////////////////////////////////////////////////////////////////////////////////////////////////////
export class Book{
    public bookId:number = -1;
    public ageRange:number[] = [0,100];
    public bookName:string = "";
    public bookAuth:string = "";
    public bookTags:string[] = [];

    public constructor(bookId:number, ageRange:number[], bookName:string, bookAuth:string,  bookTags:string[]) {
        this.bookId = bookId;
        this.ageRange = ageRange;
        this.bookName = bookName;
        this.bookAuth = bookAuth;
        this.bookTags = bookTags;
    }
}