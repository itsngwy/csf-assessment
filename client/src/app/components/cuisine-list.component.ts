import { Component, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant-service';
import { Restaurant } from '../models';

@Component({
  selector: 'app-cuisine-list',
  templateUrl: './cuisine-list.component.html',
  styleUrls: ['./cuisine-list.component.css']
})
export class CuisineListComponent implements OnInit {

  restaurants: Restaurant[] = []
  cuisineTypes: string[] = []

	// TODO Task 2
	// For View 1
  constructor(private httpSvc: RestaurantService) { }

  ngOnInit(): void {
      this.httpSvc.getCuisineList()
      .then(results => {
        console.info('>>> Cuisines:', results)
        this.restaurants = results

        this.restaurants.map(r => {
          if (this.cuisineTypes.includes(r.cuisine)) {

          } else {
            this.cuisineTypes = [...this.cuisineTypes, r.cuisine]
          }
        })
        this.cuisineTypes.sort()
        console.info(this.cuisineTypes)

      }).catch(err => {
        console.info('>>> err: ', err)
      })
  }
}
