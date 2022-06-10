import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { MsAdalAngular6Service } from 'microsoft-adal-angular6';
import { environment } from 'src/environments/environment';

@Injectable()
export class AuthInterceptor implements HttpInterceptor{
    constructor(private authService: AuthService,private adalSvc: MsAdalAngular6Service){}
    intercept(req: HttpRequest<any>,next : HttpHandler):Observable<HttpEvent<any>>{

        //this.authService.currentUserRole.subscribe(res=>{console.log('In Interceptor: userRole->',res)});

        if(!this.adalSvc.isAuthenticated){
          window.location.href=environment.logoutRedirect;
        }

        const storedData= this.authService.getAuthData();
        console.log(storedData);
        // const copiedReq= req.clone(
        //     // {headers:req.headers.append('role','admin').append('signum','ejangua')}
        //     {
        //         //headers:req.headers.append('x-auth-token',storedData.accessToken).append('role',storedData.userRole).append('signum',storedData.loggedInSignum)
        //         headers:req.headers.append('role',storedData.userRole).append('signum',storedData.loggedInSignum)
        //     }
        //     );

        if(storedData.accessToken==null){
            const copiedReq= req.clone(
                {
                    headers: new HttpHeaders({
                      'role':  storedData.userRole,
                      'signum': storedData.loggedInSignum                      
                    })
                  }
            );
            
            return next.handle(copiedReq);
        }else{
            const copiedReq= req.clone(
                {
                    headers: new HttpHeaders({
                      'role':  storedData.userRole,
                      'signum': storedData.loggedInSignum,
                      'x-auth-token':storedData.accessToken
                    })
                  }
            );
            
            return next.handle(copiedReq);
        }

        
        
    }
}