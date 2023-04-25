// package upc.edu.gessi.tfg.services;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import upc.edu.gessi.tfg.models.Parameter;
// import upc.edu.gessi.tfg.models.ParameterIntegration;
// import upc.edu.gessi.tfg.repositories.ParameterIntegrationRepository;
// import upc.edu.gessi.tfg.repositories.ParameterRepository;

// @Service
// public class ParameterService {

//     @Autowired
//     private ParameterRepository parameterRepository;

//     @Autowired
//     private ParameterIntegrationRepository parameterIntegrationRepository;

//     //PARAMETERS
//     public Iterable<Parameter> getAllParameters() {
//         return parameterRepository.findAll();
//     }

//     public Optional<Parameter> getParameterById(long id) {
//         return parameterRepository.findById(id);
//     }

//     public Parameter createParameter(Parameter parameter) {
//         return parameterRepository.save(parameter);
//     }

//     public Parameter updateParameter(long id, Parameter param) {
//         Parameter existingParameter = parameterRepository.findById(id).orElse(null);
//         if (existingParameter == null)
//             return null;
//         existingParameter.setName(param.getName());
//         existingParameter.setType(param.getType());
//         existingParameter.setValue(param.getValue());
//         return parameterRepository.save(existingParameter);
//     }

//     public void deleteParameter(long id) {
//         parameterRepository.deleteById(id);
//     }

//     //PARAMETER_INTEGRATIONS
//     public Iterable<ParameterIntegration> getAllParameterIntegrations() {
//         return parameterIntegrationRepository.findAll();
//     }

//     public Optional<ParameterIntegration> getParameterIntegrationById(long id) {
//         return parameterIntegrationRepository.findById(id);
//     }

//     public ParameterIntegration createParameterIntegration(ParameterIntegration parameterIntegration) {
//         return parameterIntegrationRepository.save(parameterIntegration);
//     }

//     public ParameterIntegration updateParameterIntegration(long id, ParameterIntegration param) {
//         ParameterIntegration existingParameterIntegration = parameterIntegrationRepository.findById(id).orElse(null);
//         if (existingParameterIntegration == null)
//             return null;
//         existingParameterIntegration.setName(param.getName());
//         existingParameterIntegration.setSourceParameter(param.getSourceParameter());
//         existingParameterIntegration.setTargetParameter(param.getTargetParameter());
//         existingParameterIntegration.setValue(param.getValue());
//         return parameterIntegrationRepository.save(existingParameterIntegration);
//     }

//     public void deleteParameterIntegration(long id) {
//         parameterIntegrationRepository.deleteById(id);
//     }
// }
