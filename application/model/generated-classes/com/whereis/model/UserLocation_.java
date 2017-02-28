package com.whereis.model;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserLocation.class)
public abstract class UserLocation_ {

	public static volatile SingularAttribute<UserLocation, Double> latitude;
	public static volatile SingularAttribute<UserLocation, String> ip;
	public static volatile SingularAttribute<UserLocation, Integer> id;
	public static volatile SingularAttribute<UserLocation, User> user;
	public static volatile SingularAttribute<UserLocation, Timestamp> timestamp;
	public static volatile SingularAttribute<UserLocation, Double> longitude;

}

