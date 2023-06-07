package upc.edu.gessi.tfg.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upc.edu.gessi.tfg.models.App;
import upc.edu.gessi.tfg.models.AppIntegration;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.models.User;
import upc.edu.gessi.tfg.repositories.AppIntegrationRepository;
import upc.edu.gessi.tfg.repositories.AppRepository;
import upc.edu.gessi.tfg.repositories.FeatureIntegrationRepository;
import upc.edu.gessi.tfg.repositories.FeatureRepository;
import upc.edu.gessi.tfg.repositories.ParameterIntegrationRepository;
import upc.edu.gessi.tfg.repositories.UserRepository;

@Service
public class UserAppService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppIntegrationRepository appIntegrationRepository;

    @Autowired
    private FeatureIntegrationRepository featureIntegrationRepository;

    @Autowired
    private ParameterIntegrationRepository parameterIntegrationRepository;

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
    
    public User getUserById(String id) {
        return userRepository.getUser(id);
    }

    public void createUser(User user) {
        userRepository.createUser(user);
    }

    public void updateUser(String id, User user) {
        if (user.getIdentifier() == null)
            user.setIdentifier(id);
        userRepository.updateUser(id, user);
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }

    //Preferred Operations
    public void addPreferredAppIntegration(String id, AppIntegration appIntegration) {
        appIntegration.setIdentifier(appIntegration.getSourceApp()+"-"+appIntegration.getTargetApp());
        appIntegrationRepository.addPreferredAppIntegration(id, appIntegration);    
    }

    public void deletePreferredAppIntegration(String id, AppIntegration appIntegration) {
        appIntegration.setIdentifier(appIntegration.getSourceApp()+"-"+appIntegration.getTargetApp());
        appIntegrationRepository.removePreferredAppIntegration(id, appIntegration);
    }

    public void addPreferredFeatureIntegration(String id, FeatureIntegration featureIntegration) {
        featureIntegration.setIdentifier(featureIntegration.getSourceFeature()+"-"+featureIntegration.getTargetFeature());
        featureIntegrationRepository.addPreferredFeatureIntegration(id, featureIntegration);
    }

    public void deletePreferredFeatureIntegration(String id, FeatureIntegration featureIntegration) {
        featureIntegration.setIdentifier(featureIntegration.getSourceFeature()+"-"+featureIntegration.getTargetFeature());
        featureIntegrationRepository.removePreferredFeatureIntegration(id, featureIntegration);
    }

    public void addPreferredParameterIntegration(String id, ParameterIntegration parameterIntegration) {
        parameterIntegration.setIdentifier(parameterIntegration.getSourceParameter()+"-"+parameterIntegration.getTargetParameter());
        parameterIntegrationRepository.addPreferredParameterIntegration(id, parameterIntegration);
    }

    public void deletePreferredParameterIntegration(String id, ParameterIntegration parameterIntegration) {
        parameterIntegration.setIdentifier(parameterIntegration.getSourceParameter()+"-"+parameterIntegration.getTargetParameter());
        parameterIntegrationRepository.removePreferredParameterIntegration(id, parameterIntegration);
    }

    public List<App> getAllApps() {
        return appRepository.getAllApps();
    }

    public App getAppById(String id) {
        return appRepository.getApp(id);
    }

    public void createApp(App app) {
        appRepository.createApp(app);
    }

    public void updateApp(String id, App app) {
        if (app.getIdentifier() == null)
            app.setIdentifier(id);
        appRepository.updateApp(id, app);
    }

    public void deleteApp(String id) {
        appRepository.deleteApp(id);
    }

    public List<AppIntegration> getAllAppIntegrations() {
        return appIntegrationRepository.getAllAppIntegrations();
    }

    public AppIntegration getAppIntegrationById(String id) {
        return appIntegrationRepository.getAppIntegration(id);
    }

    public void createAppIntegration(AppIntegration appIntegration) {
        appIntegration.setIdentifier(appIntegration.getSourceApp()+"-"+appIntegration.getTargetApp());
        appIntegrationRepository.createAppIntegration(appIntegration);
    }

    public void updateAppIntegration(String id, AppIntegration appIntegration) {
        if (appIntegration.getIdentifier() == null)
            appIntegration.setIdentifier(id);
        if (appIntegration.getName() == null)
            appIntegration.setName(id);
        appIntegrationRepository.updateAppIntegration(id, appIntegration);
    }

    public void deleteAppIntegration(String id) {
        appIntegrationRepository.removeAppIntegration(id);
    }

    //USER STORY #1
    public List<String> requestFeatureIntegration(String user, String feature) {
        return featureIntegrationRepository.requestIntegrationsTargetFeatures(user, feature);
    }

    //USER STORY #2
    public List<String> getAppsFromFeature(String user, String sourceFeature, String feature) {
        return appIntegrationRepository.getAppsFromFeature(user, sourceFeature, feature);
    }

}
