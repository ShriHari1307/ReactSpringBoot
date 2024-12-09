package com.example.springReactBackEnd.Controller;

import com.example.springReactBackEnd.DTO.AgentDTO;
import com.example.springReactBackEnd.DTO.ProviderDTO;
import com.example.springReactBackEnd.Entity.Agent;
import com.example.springReactBackEnd.Entity.Provider;
import com.example.springReactBackEnd.Exception.AgentManagementException;
import com.example.springReactBackEnd.Exception.AgentNotFoundException;
import com.example.springReactBackEnd.Exception.ProviderManagementException;
import com.example.springReactBackEnd.Exception.ProviderNotFoundException;
import com.example.springReactBackEnd.Response.ResponseHandler;
import com.example.springReactBackEnd.Service.AgentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping("/create")
    public ResponseEntity<Object> createAgent(@RequestBody AgentDTO agentDTO) throws AgentManagementException {
        try {
            AgentDTO createdAgent = agentService.createAgent(agentDTO);
            return ResponseHandler.getResponse("Agent created Successfully",HttpStatus.CREATED,createdAgent);
        }
        catch (Exception e){
            return ResponseHandler.getResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/search")
    public List<AgentDTO> searchAgentsByName( @RequestParam String firstName){
        return agentService.searchAgentsByName(firstName);
    }


    @GetMapping("/{agentId}")
    public ResponseEntity<Object> getAgent(@PathVariable Long agentId,HttpServletRequest request) throws AgentNotFoundException, AgentManagementException {
        AgentDTO agentDTO = agentService.findAgent(agentId);
        return ResponseHandler.getResponse("Agent found", HttpStatus.OK, agentDTO);
    }
    @GetMapping("/count")
    public ResponseEntity<Integer> agentCount() {
        List<AgentDTO> agentCount = agentService.findAllAgents();
        return new ResponseEntity<>(agentCount.size(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllAgents() throws AgentManagementException {
        List<AgentDTO> agents = agentService.findAllAgents();
        return ResponseHandler.getResponse("Agents retrieved successfully", HttpStatus.OK, agents);
    }

    @DeleteMapping("/deleteAgent/{agentId}")
    public ResponseEntity<Object> deleteProvider(@PathVariable Long agentId) throws AgentNotFoundException, AgentManagementException {
        System.out.println("req del received");
//        log.info("Delete function called");
//        log.debug("Deleting provider by ID: " + providerId);
        List<AgentDTO> agents = agentService.findAllAgents();
//        log.info("Total providers retrieved before deletion: " + providers.size());
//        for (ProviderDTO provider : providers) {
//            log.debug("Details of providers before deletion: " + providers);
//        }
        List<Agent> deletedAgent = agentService.deleteAgent(agentId);
//        List<ProviderDTO> providersAfter = providerService.findAllProviders();
//        log.info("Provider deleted successfully: " + deletedProvider);
//        log.info("Total providers retrieved after deletion: " + providersAfter.size());
//        for (ProviderDTO providerDTO : providersAfter) {
//            log.info("Details of providers after deletion: " + providersAfter);
//        }
        return ResponseHandler.getResponse("Agent deleted successfully", HttpStatus.OK, deletedAgent);
    }

    @DeleteMapping("/deleteAgentByName")
    public ResponseEntity<?> deleteAgentByName(@RequestParam String firstName, @RequestParam String lastName) {
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Agent's first name and last name must be provided.");
        }

        agentService.removeAgentByName(firstName,lastName);
        return ResponseEntity.ok("Agent deleted successfully.");
    }

    @PutMapping("/update/{agentId}")
    public ResponseEntity<Object> updateProvider(@PathVariable Long agentId, @RequestBody AgentDTO agentDTO) {
        try {
            AgentDTO updatedAgent = agentService.updateAgent(agentId, agentDTO);
            return ResponseHandler.getResponse("Agent updated successfully", HttpStatus.OK, updatedAgent);
        } catch (AgentNotFoundException ex) {
            return ResponseHandler.getResponse("Agent not found", HttpStatus.NOT_FOUND, null);
        } catch (AgentManagementException ex) {
            return ResponseHandler.getResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception ex) {
            System.err.println("Unexpected error during provider update: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseHandler.getResponse("An unexpected error occurred: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


}
