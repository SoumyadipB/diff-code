import { Component, OnInit } from '@angular/core';
import { NgForm } from "@angular/forms";
import { Router } from '@angular/router';

import { AuthService } from "../auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  isLoading=false;

  constructor(public authService: AuthService,private router: Router) {}

  onLogin(form: NgForm) {
    if (form.invalid) {
      return;
    }
    this.isLoading = true;
    this.authService.login(form.value.email, form.value.password).subscribe(response => {
      if(this.authService.createToken(response)){
        this.router.navigate(["/"]);
      }else{
        this.isLoading = false;
      }
    },
      error => {
        this.isLoading = false;
      }
    );
  }

  
  ngOnInit() {
  }

}

