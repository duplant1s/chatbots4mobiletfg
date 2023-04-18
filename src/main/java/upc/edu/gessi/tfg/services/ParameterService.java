package upc.edu.gessi.tfg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upc.edu.gessi.tfg.models.Parameter;
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.repositories.GraphDBService;

@Service
public class ParameterService {

    @Autowired
    private GraphDBService graphDBService;

    //PARAMETERS
    public Parameter getParameterById(long id) {
        return graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parameter", "id", id));
    }

    public Parameter createParameter(Parameter parameter) {
        return graphDBService.save(parameter);
    }

    public Parameter updateParameter(long id, Parameter parameter) {
        Parameter existingParameter = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parameter", "id", id));
        existingParameter.setName(parameter.getName());
        existingParameter.setDescription(parameter.getDescription());
        return graphDBService.save(existingParameter);
    }

    public Parameter deleteParameter(long id) {
        Parameter existingParameter = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parameter", "id", id));
        graphDBService.delete(existingParameter);
        return existingParameter;
    }

    //PARAMETER_INTEGRATIONS
    public ParameterIntegration getParameterIntegrationById(long id) {
        return graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParameterIntegration", "id", id));
    }

    public ParameterIntegration createParameterIntegration(ParameterIntegration parameterIntegration) {
        return graphDBService.save(parameterIntegration);
    }

    public ParameterIntegration updateParameterIntegration(long id, ParameterIntegration parameterIntegration) {
        ParameterIntegration existingParameterIntegration = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParameterIntegration", "id", id));
        existingParameterIntegration.setParameter(parameterIntegration.getParameter());
        existingParameterIntegration.setIntegration(parameterIntegration.getIntegration());
        return graphDBService.save(existingParameterIntegration);
    }

    public ParameterIntegration deleteParameterIntegration(long id) {
        ParameterIntegration existingParameterIntegration = graphDBService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParameterIntegration", "id", id));
        graphDBService.delete(existingParameterIntegration);
        return existingParameterIntegration;
    }
}
