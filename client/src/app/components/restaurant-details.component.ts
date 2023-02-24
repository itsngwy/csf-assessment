import { Component, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant-service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent implements OnInit {
	
	// TODO Task 4 and Task 5
	// For View 3
  params$!: Subscription

  constructor(private httpSvc: RestaurantService, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
      
    this.params$ = this.activatedRoute.params.subscribe(
      params => {
        const restaurantName = params['restaurantName']
        this.httpSvc.getRestaurant(restaurantName)
        .then(results => {
          console.info(results)
        })
      }
    )
  }
}
