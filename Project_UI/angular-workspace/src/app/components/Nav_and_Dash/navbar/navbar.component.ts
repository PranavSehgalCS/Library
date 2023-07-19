import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'Navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(private router:Router,
              private accService:AccountService){
    if(!accService.isLoggedIn()){
      this.router.navigate(['/login']);
    }else{
    }
  }

  public log_out(){
    if(confirm("Are You Sure You Want To Log Out?")){
      this.accService.logout();
      this.router.navigate(['/login']);
    }
  }
  public isAdmin():boolean{
    return this.accService.isAdmin();
  }

  public logged():boolean{
    return this.accService.isLoggedIn()
  }
  public move_to(loc:string){
    this.router.navigate(['/'+loc])
  }
}
