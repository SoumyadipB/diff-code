import { Component } from '@angular/core';
import { MsAdalAngular6Service } from 'microsoft-adal-angular6';
import { AuthService } from './auth/auth.service';
import {ConnectionService} from 'ng-connection-service'
import { AlertBoxService } from 'src/app/shared/alert-box.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'startNow';
  constructor(private adalSvc: MsAdalAngular6Service, private authService : AuthService,private conn:ConnectionService,private alertBoxService: AlertBoxService) {

    this.conn.monitor().subscribe(connectivity=>{
      connectivity ? this.alertBoxService.connectionAlert("Success",''):this.alertBoxService.connectionAlert("Error",'Internet Connection Lost');
      });

    //console.log(this.adalSvc.userInfo);

    // console.log(this.adalSvc.isAuthenticated);
    // console.log(this.adalSvc.LoggedInUserEmail);

    // if(this.adalSvc.isAuthenticated){
      
    //   this.authService.loginAfterAdal(this.adalSvc.LoggedInUserEmail);

    // }

    //console.log(environment.authConfig);
    //const userEmail=this.adalSvc.userInfo.profile.unique_name;
   
  }
}
