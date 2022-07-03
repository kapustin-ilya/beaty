package web.controller;

import web.controller.commads.*;

import java.util.HashMap;
import java.util.Map;

public class MapCommand {

    static Map<String, Command> commandMap = new HashMap<String, Command>();

    static {
        commandMap.put("category", new CategoryController());
        commandMap.put("login", new LogInController());
        commandMap.put("registration", new RegistrationController());
        commandMap.put("logout", new LogOutController());
        commandMap.put("userCabinet", new UserCabinetController());
        commandMap.put("recordingCabinet", new RecordingCabinetController());
        commandMap.put("newRecording", new NewRecordingController());
        commandMap.put("masterCabinet", new MasterCabinetController());
        commandMap.put("masterCompleted", new MasterCompletedController());
        commandMap.put("userComment", new UserCommentController());
        commandMap.put("adminCabinet", new AdminCabinetController());
        commandMap.put("adminCompleted", new AdminCompletedController());
        commandMap.put("profileCabinet", new ProfileCabinetController());
        commandMap.put("profileUpdate", new ProfileUpdateController());
        commandMap.put("local", new LocalController());
    }

    public static Command getCommand(String nameCommand){
        return commandMap.get(nameCommand);
    }
}
