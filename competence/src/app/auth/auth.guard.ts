import {
    CanActivate,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
    Router
  } from "@angular/router";
  import { Injectable } from "@angular/core";
  import { Observable } from "rxjs";
  
  import { AuthService } from "./auth.service";
  
  @Injectable()
  export class AuthGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) {}
  
    canActivate(
      route: ActivatedRouteSnapshot,
      state: RouterStateSnapshot
    ): boolean | Observable<boolean> | Promise<boolean> {
      // const isAuth = this.authService.getIsAuth();
      // if (!isAuth) {
      //   this.router.navigate(['/login']);
      // }
      // return isAuth;

      //console.log('File AuthGuard-> getAuthData()', this.authService.getAuthData())
      const userRole=this.authService.getAuthData().userRole;
      //console.log('AuthGuard',userRole);

      if(userRole==="LM" && state.url==="/competence"){
        //this.router.navigate(['/competence-request']);
        return false;
      }
      if(userRole==="User" && state.url==="/competence-request"){
        //this.router.navigate(['/competence']);
        return false;
      }
            
      return true;
    }
  }