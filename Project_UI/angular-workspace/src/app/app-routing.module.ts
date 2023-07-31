/** ///////////////////////////////////////////////////////////////////////////////////////////////////////
 *  FILE : app-routing.module.ts
 *  AUTHOR : Pranav Sehgal
 *           + Auto-generated on ng create if angular routing is selected
 *  DESCRIPTION : USED to select component to display within index.html
 *                CHANGES bases on URL path
 ///////////////////////////////////////////////////////////////////////////////////////////////////////*/
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './components/Account/login/login.component';
import { SignupComponent } from './components/Account/signup/signup.component';
import { AccPageComponent } from './components/Account/acc-page/acc-page.component';

import { ViewTagComponent } from './components/Tag/view-tag/view-tag.component';
import { CreateTagComponent } from './components/Tag/create-tag/create-tag.component';
import { UpdateTagComponent } from './components/Tag/update-tag/update-tag.component';

import { BorrowComponent } from './components/Book/borrow/borrow.component';
import { EditBookComponent } from './components/Book/edit-book/edit-book.component';
import { ViewBookComponent } from './components/Book/view-book/view-book.component';
import { CreateBookComponent } from './components/Book/create-book/create-book.component';
import { DashboardComponent } from './components/Nav_and_Dash/dashboard/dashboard.component';
const routes: Routes = [
  
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path:'login', component:LoginComponent },
  { path:'signup', component:SignupComponent },
  { path:'accPage', component:AccPageComponent},

  { path:'tags/view', component:ViewTagComponent},
  { path:'tags/edit', component:UpdateTagComponent},
  { path:'tags/create', component:CreateTagComponent},

  { path:'books/view', component:ViewBookComponent},
  { path:'books/edit', component:EditBookComponent},
  { path:'books/borrow', component:BorrowComponent},
  { path:'books/create', component:CreateBookComponent},
  
  { path:'dashboard', component:DashboardComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }