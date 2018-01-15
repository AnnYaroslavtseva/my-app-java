package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Fibonacci;
import models.FibonacciStore;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.Util;

import java.util.Set;


public class FibonacciController extends Controller {
    public Result create() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest(Util.createResponse("Expecting Json data", false));
        }
        Fibonacci fibonacci = FibonacciStore.getInstance().addNote(Json.fromJson(json, Fibonacci.class));
        JsonNode jsonObject = Json.toJson(fibonacci);
        return created(Util.createResponse(jsonObject, true));
    }

    public Result update() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest(Util.createResponse("Expecting Json data", false));
        }
        Fibonacci fibonacci = FibonacciStore.getInstance().updateNote(Json.fromJson(json, Fibonacci.class));
        if (fibonacci == null) {
            return notFound(Util.createResponse("Sequence not found", false));
        }

        JsonNode jsonObject = Json.toJson(fibonacci);
        return ok(Util.createResponse(jsonObject, true));
    }

    public Result retrieve(int parameterN) {
        if (FibonacciStore.getInstance().getNote(parameterN) == null) {
            return notFound(Util.createResponse("Sequence with parameter:" + parameterN + " not found", false));
        }
        JsonNode jsonObjects = Json.toJson(FibonacciStore.getInstance().getNote(parameterN));
        return ok(Util.createResponse(jsonObjects, true));
    }

    public Result listSequences() {
        Set<Fibonacci> result = FibonacciStore.getInstance().getAllNotes();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        return ok(Util.createResponse(jsonData, true));

    }

    public Result delete(int parameterN) {
        if (!FibonacciStore.getInstance().deleteNote(parameterN)) {
            return notFound(Util.createResponse("Sequence with parameter:" + parameterN + " not found", false));
        }
        return ok(Util.createResponse("Sequence with id:" + parameterN + " deleted", true));
    }

}
