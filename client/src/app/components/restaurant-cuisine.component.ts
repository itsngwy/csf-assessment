import { Component, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant-service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Restaurant } from '../models';

@Component({
  selector: 'app-restaurant-cuisine',
  templateUrl: './restaurant-cuisine.component.html',
  styleUrls: ['./restaurant-cuisine.component.css']
})
export class RestaurantCuisineComponent implements OnInit {
	
	// TODO Task 3
	// For View 2
  params$!: Subscription
  restaurants: Restaurant[] = []
  cuisine: string = ''
  restaurantNames: string[] = []

  constructor(private httpSvc: RestaurantService, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {

    this.params$ = this.activatedRoute.params.subscribe(
      params => {
        const cuisineName = params['cuisineName']
        this.cuisine = cuisineName
        this.httpSvc.getRestaurantsByCuisine(cuisineName)
        .then(results => {
          console.info('>>> Restaurants:', results)
          this.restaurants = results

          this.restaurants.map(r => {
            if (this.restaurantNames.includes(r.name)) {
  
            } else {
              this.restaurantNames = [...this.restaurantNames, r.name]
            }
          })
          this.restaurantNames.sort()
        })
      }
    )
    this.restaurants.sort()
  }
}
