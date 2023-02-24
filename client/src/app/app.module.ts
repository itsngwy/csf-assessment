import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { CuisineListComponent } from './components/cuisine-list.component';
import { RestaurantCuisineComponent } from './components/restaurant-cuisine.component';
import { RestaurantDetailsComponent } from './components/restaurant-details.component';
import { RestaurantService } from './restaurant-service';
import { RouterModule, Routes } from '@angular/router'

const appRoutes: Routes = [
  { path: '', component: CuisineListComponent },
  { path: 'cuisine/:cuisineName', component: RestaurantCuisineComponent },
  { path: 'restaurant/:restaurantName', component: RestaurantDetailsComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    CuisineListComponent,
    RestaurantCuisineComponent,
    RestaurantDetailsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [ RestaurantService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
