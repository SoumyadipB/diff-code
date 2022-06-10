import { Component, OnInit, Input } from '@angular/core';
import { Subscription, Observable } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';
import { MsAdalAngular6Service } from 'microsoft-adal-angular6';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-menu-links',
  templateUrl: './menu-links.component.html',
  styleUrls: ['./menu-links.component.css']
})
export class MenuLinksComponent implements OnInit {
  @Input() classOnMenu:string = '';
  userIsAuthenticated = false;
  private authListenerSubs: Subscription;
  // userDetailsObj :any;
  // userRole='';
  // uName='';
  // signum='';
  constructor(private authService: AuthService,private adalService:MsAdalAngular6Service, private router: Router) {}

  UserName$ : Observable<string>;
  signum$ : Observable<string>;
  userRole$ : string;

  ngOnInit() {
    // this.userDetailsObj=this.authService.getAuthData();
    // this.userRole=this.userDetailsObj.userRole;
    // this.uName=this.userDetailsObj.empName;
    // this.signum=this.userDetailsObj.loggedInSignum;

    this.UserName$ = this.authService.currentUserName;
    this.signum$=this.authService.currentUserSignum;

    this.authService.currentUserRole.subscribe(res=>{this.userRole$=res;});
    

    // this.userIsAuthenticated = this.authService.getIsAuth();
    // this.authListenerSubs = this.authService
    //   .getAuthStatusListener()
    //   .subscribe(isAuthenticated => {
    //     this.userIsAuthenticated = isAuthenticated;
    //   });
  }

  onLogout() {
    this.authService.logout();
  }
  
  onADFSLogout(){
    this.adalService.logout();
    this.authService.logout();
    
  }

  logoutRedirect(){
    window.location.href=environment.logoutRedirect;
  }


  ngOnDestroy() {
    this.authListenerSubs.unsubscribe();
  }

}
