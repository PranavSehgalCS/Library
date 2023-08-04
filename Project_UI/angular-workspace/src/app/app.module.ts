/**
 * ///////////////////////////////////////////////////////////////////////////////////////////////////////
 *  FILE : app.module.ts
 *  AUTHOR :  Pranav Sehgal 
 *            + Auto-generated on ng create
 *            + Auto-Editied on ng generate
 *  DESCRIPTION : Used to handle angular imports, exports and bindings.
 //////////////////////////////////////////////////////////////////////////////////////////////////////*/

import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { SignupComponent } from './components/Account/signup/signup.component';
import { LoginComponent } from './components/Account/login/login.component';
import { AccPageComponent } from './components/Account/acc-page/acc-page.component';
import { NavbarComponent } from './components/Nav_and_Dash/navbar/navbar.component';

import { CreateBookComponent } from './components/Book/create-book/create-book.component';
import { EditBookComponent } from './components/Book/edit-book/edit-book.component';
import { ViewBookComponent } from './components/Book/view-book/view-book.component';
import { BorrowComponent } from './components/Book/borrow/borrow.component';

import { ViewTagComponent } from './components/Tag/view-tag/view-tag.component';

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    LoginComponent,
    AccPageComponent,
    NavbarComponent,
    CreateBookComponent,
    EditBookComponent,
    ViewBookComponent,
    BorrowComponent,
    ViewTagComponent,
    ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [
    AppComponent
  ]
})

export class AppModule {
  
}
