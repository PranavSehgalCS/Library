///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : Account.ts
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a template ts Account model with constructor to encapsulate data 
///////////////////////////////////////////////////////////////////////////////////////////////////////
export class Account{
    public accId:number = -1;
    public accName:string = "";
    public accPass:string = "";
    public isAdmin:boolean = false;

    public constructor(accId:number, accName:string ,accPass:string, isAdmin:boolean) {
        this.accId = accId;
        this.accName = accName;
        this.accPass = accPass;
        this.isAdmin = isAdmin;
    }
}