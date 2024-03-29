package upc.edu.gessi.tfg.services;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upc.edu.gessi.tfg.models.Parameter;
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.repositories.ParameterIntegrationRepository;
import upc.edu.gessi.tfg.repositories.ParameterRepository;

@Service
public class ParameterService {

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterIntegrationRepository parameterIntegrationRepository;

    //PARAMETERS
    //CRUDS/////////////////
    public List<Parameter> getAllParameters() {
        return parameterRepository.getAllParameters();
    }

    public Parameter getParameter(String id) {
        return parameterRepository.getParameter(id);
    }

    public void createParameter(Parameter parameter) {
        if (parameter.getIdentifier() == null)
            parameter.setIdentifier(parameter.getName());
        parameterRepository.createParameter(parameter);
    }

    public void updateParameter(String id, Parameter param) {
        if (param.getIdentifier() == null)
            param.setIdentifier(id);
        parameterRepository.updateParameter(id, param);
    }

    public void deleteParameter(String id) {
        parameterRepository.deleteParameter(id);
    }
    

    //PARAMETER_INTEGRATIONS

    ////////////////////////
    //CRUDS/////////////////
    public List<ParameterIntegration> getAllParameterIntegrations() {
        return parameterIntegrationRepository.getAllParameterIntegrations();
    }

    public ParameterIntegration getParameterIntegration(String id) {
        return parameterIntegrationRepository.getParameterIntegration(id);
    }

    public void createParameterIntegration(ParameterIntegration parameterIntegration) {
        parameterIntegrationRepository.createParameterIntegration(parameterIntegration);
    }

    public void updateParameterIntegration(String id, ParameterIntegration param) {
        if (param.getIdentifier() == null)
            param.setIdentifier(id);
        if (param.getName() == null)
            param.setName(id);
        parameterIntegrationRepository.updateParameterIntegration(id, param);
    }

    public void deleteParameterIntegration(String id) {
        parameterIntegrationRepository.deleteParameterIntegration(id);
    }

    ////////////////////////

    //US3
    public List<List<Object>> getParameterIntegrations(String sourceApp, String sourceFeature, String targetApp, String targetFeature) {
        return parameterIntegrationRepository.requestParameterIntegration(sourceApp, sourceFeature, targetApp, targetFeature);
    }

    //US4
    public List<String> getCustomParameters(String sourceApp, String sourceFeature, String targetApp, String targetFeature) {
        return parameterIntegrationRepository.requestCustomParameters(sourceApp, sourceFeature, targetApp, targetFeature);
    }
}
