package com.swarup.e_restaurants.model;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "mst_location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int cityId;
    private String location;
    private int pin;
    private boolean status;

}
