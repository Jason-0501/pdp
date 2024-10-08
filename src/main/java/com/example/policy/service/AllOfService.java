package com.example.policy.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.policy.model.AllOf;
import com.example.policy.model.AnyOf;
import com.example.policy.model.Match;
import com.example.policy.repository.AllOfRepository;
import com.example.policy.repository.AnyOfRepository;
import com.example.policy.repository.MatchRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AllOfService {
	@Autowired
	private AllOfRepository allOfRepository;
	
	@Autowired
	private MatchRepository matchRepository;
	
	@Autowired
	private AnyOfRepository anyOfRepository;
	
	public List<AllOf> getAllAllOfs(){
		return allOfRepository.findAll();
	}
	public Optional<AllOf> getAllOfById(Long id) {
	      return allOfRepository.findById(id);
	  }
	public AllOf createAllOf(Map<String, Integer> requestMap){
		Long anyOfId = Long.parseLong(requestMap.get("anyOf").toString());
		Optional<AnyOf> anyof = anyOfRepository.findById(anyOfId);
		if(anyof.isEmpty()) {
			throw new EntityNotFoundException("AnyOf not found");
		}
		AllOf allOf = new AllOf();
		allOf.setAnyOf(anyof.get());
		return allOfRepository.save(allOf);
	}
	
	public Optional<AllOf> updateAllOf(Long id, AllOf AllOf) {
		 return allOfRepository.findById(id)
	                .map(existingAllOf -> {
	                    existingAllOf.setMatches(AllOf.getMatches());
	                    return allOfRepository.save(existingAllOf);
	                });
	}
	public void deleteAllOf(Long id) {
		 Optional<AllOf> allof=allOfRepository.findById(id);
		 if(allof.isPresent()) {
			List<Match> matchList = allof.get().getMatches();
			Iterator<Match> iterator = matchList.iterator();
	        while (iterator.hasNext()) {
	        	Match match = iterator.next();
	            iterator.remove();  // 從列表中移除 Match
	            matchRepository.delete(match);  // 刪除 Match
	        }
	        AnyOf anyof = allof.get().getAnyOf();
	        anyof.getAllOfs().remove(allof.get());
			allOfRepository.deleteById(id);
		 }else {
			 throw new EntityNotFoundException("AllOf not found with id: " + id);
		 }
		 
	}
}
