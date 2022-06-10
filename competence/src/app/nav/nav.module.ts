import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { NavComponent } from '../nav/nav.component';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
// import {MatTabsModule} from '@angular/material/tabs';



import { MenuLinksComponent } from './menu-links/menu-links.component';



@NgModule({
    declarations:[
        NavComponent,
        MenuLinksComponent
    ],
    imports:[
        CommonModule,
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatIconModule,
        MatListModule,
        // MatTabsModule,
        RouterModule,

    ],
    exports:[
        NavComponent
    ]
})

export class navModule{

}