///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : Templates.ts
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a ts model for tags with constructor to encapsulate data 
///////////////////////////////////////////////////////////////////////////////////////////////////////

export class Tag{
    public tagId:number = 0;
    public tagName:string = "";

    public constructor(tagId:number, tagName:string) {
        this.tagId = tagId;
        this.tagName = tagName;
    }
}