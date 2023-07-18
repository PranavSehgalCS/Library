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

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    LoginComponent,
    AccPageComponent
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
