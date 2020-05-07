package model.message;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

import java.util.List;
import model.message.actuation.RecActuationCommand;
import model.message.actuation.RecActuationResponse;
import model.message.exception.RecException;
import model.message.modulestate.ModuleMessage;

public class RecMessage {

  private final String format = "rec3.2";
  private String deviceId;
  private List<RecObservation> observations;
  private List<RecException> exceptions;
  private List<RecActuationCommand> actuationCommands;
  private List<RecActuationResponse> actuationResponses;
  private ModuleMessage edgeStatus;

  public String getFormat() {
    return format;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public RecMessage setDeviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  public List<RecObservation> getObservations() {
    return isNull(observations)
        ? emptyList()
        : observations;
  }

  public RecMessage setObservations(List<RecObservation> observations) {
    this.observations = observations;
    return this;
  }

  public List<RecException> getExceptions() {
    return isNull(exceptions)
        ? emptyList()
        : exceptions;
  }

  public List<RecActuationCommand> getActuationCommands() {
    return isNull(actuationCommands)
        ? emptyList()
        : actuationCommands;
  }

  public List<RecActuationResponse> getActuationResponses() {
    return isNull(actuationResponses)
        ? emptyList()
        : actuationResponses;
  }

  public RecMessage setActuationResponses(List<RecActuationResponse> actuationResponses) {
    this.actuationResponses = actuationResponses;
    return this;
  }

  public ModuleMessage getEdgeStatus() {
    return edgeStatus;
  }
}