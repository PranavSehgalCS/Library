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

import { DashboardComponent } from './components/Nav_and_Dash/dashboard/dashboard.component';
const routes: Routes = [
  
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path:'login', component:LoginComponent },
  { path:'signup', component:SignupComponent },
  { path:'accPage', component:AccPageComponent},
  { path:'dashboard', component:DashboardComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }