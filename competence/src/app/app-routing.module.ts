import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { WelcomeComponent } from './welcome/welcome.component';
import { MyCompetenceComponent } from './my-competence/my-competence.component';
import { LoginComponent } from './auth/login/login.component';
import { AuthGuard } from './auth/auth.guard';
import {CompetenceRequestComponent} from './competence-request/competence-request.component';
import { AuthenticationGuard } from 'microsoft-adal-angular6';
import { RefreshPageComponent } from './refresh-page/refresh-page.component';

const routes: Routes = [
  { path: '', component: WelcomeComponent,canActivate: [AuthenticationGuard,AuthGuard]  },
  { path: 'refresh-page', component: RefreshPageComponent },
  //{ path: 'competence', component: MyCompetenceComponent ,canActivate: [AuthGuard]},
  { path: 'competence', component: MyCompetenceComponent,canActivate: [AuthenticationGuard,AuthGuard] },
  { path: 'login', component: LoginComponent },
  {path:'competence-request', component:CompetenceRequestComponent,canActivate: [AuthenticationGuard,AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers:[AuthGuard]
})
export class AppRoutingModule { }
