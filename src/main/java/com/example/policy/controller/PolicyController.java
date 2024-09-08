package com.example.policy.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.policy.model.Policy;
import com.example.policy.model.RequestContext;
import com.example.policy.service.PolicyService;

@RestController
@RequestMapping("policies")
public class PolicyController {

	@Autowired
	private PolicyService policyService;
	
	@GetMapping
	public List<Policy> getAllPolicies(){
		return policyService.getAllPolicies();
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<Policy> getUserById(@PathVariable Long id) {
		Optional<Policy> PolicyOptional = policyService.getPolicyById(id);
		return PolicyOptional.map(Policy -> ResponseEntity.ok(Policy))
                .orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	 @PostMapping
	 @ResponseStatus(HttpStatus.CREATED)
	 public Policy createPolicy(@RequestBody Policy Policy) {
	      return policyService.createPolicy(Policy);
	 }
	 
	 @PutMapping("/{id}")
	 public ResponseEntity<Policy> updatePolicy(@PathVariable Long id, @RequestBody Policy Policy) {
		 Optional<Policy> updatedPolicy = policyService.updatePolicy(id, Policy);
		 return updatedPolicy.map(updated -> ResponseEntity.ok(updated))
                 .orElseGet(() -> ResponseEntity.notFound().build());
	 }
	 
	 @DeleteMapping("/{id}")
	 public void deletePolicy(@PathVariable Long id) {
		 policyService.deletePolicy(id);

	 }
	 
	 @PostMapping("/check")
	 public boolean checkPolicies(@RequestBody RequestContext context) throws Exception {
		 List<Policy> set = policyService.getAllPolicies();
		 for(Policy s : set) {
			 if(!s.evaluate(context)) return false;
		 }
		 
		 return true;
	 }
	 
}