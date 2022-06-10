import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface AuthData {
  email: string;
  password: string;
}

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class AuthDataModule { }
