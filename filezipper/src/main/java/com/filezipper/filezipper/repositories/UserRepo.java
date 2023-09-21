package com.filezipper.filezipper.repositories;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.filezipper.filezipper.model.User;

@Repository
public interface  UserRepo extends  MongoRepository<User, ObjectId> {

	public User findByUsername(String username);
}
