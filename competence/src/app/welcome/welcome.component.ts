import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { MsAdalAngular6Service } from 'microsoft-adal-angular6';


@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router, private adalSvc: MsAdalAngular6Service) { }

  ngOnInit() {

    //console.log('file:welcome.c.ts-> userRole', this.authService.getAuthData().userRole);

    // console.log(this.adalSvc.isAuthenticated);
    // console.log(this.adalSvc.LoggedInUserEmail);

    if (this.adalSvc.isAuthenticated) {

      this.authService.loginAfterAdal(this.adalSvc.LoggedInUserEmail);

      // if (this.authService.getAuthData().userRole === "User") {

      //   this.router.navigate(["/competence"]);

      // } else if(this.authService.getAuthData().userRole === "LM") {

      //   this.router.navigate(["/competence-request"]);
      // }else if(this.authService.getAuthData().userRole === null){
      //   this.router.navigate(["/welcome"]);
      // }

    }




  }

}
