package com.altres.event;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import com.altres.rs.model.ReservationDetails;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.FixedUidGenerator;
import net.fortuna.ical4j.util.HostInfo;
import net.fortuna.ical4j.util.InetAddressHostInfo;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import net.fortuna.ical4j.util.UidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

public class CalenderEvent {

  private static final Logger LOGGER = Logger.getLogger(CalenderEvent.class.getName());

  public static String getAttachment(ReservationDetails reservationDetails, String email) {
    System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
    TimeZone zone = TimeZoneRegistryFactory.getInstance().createRegistry()
        .getTimeZone(ZoneId.systemDefault().toString());
    VTimeZone vTimeZone = zone.getVTimeZone();
    return new CalenderEvent().setEvent(reservationDetails, email, vTimeZone, zone);
  }

  private String setEvent(ReservationDetails reservationDetails, String email, VTimeZone vTimeZone, TimeZone zone) {

    LocalDate startDay = LocalDate.parse(reservationDetails.getStartDate());
    LocalTime startTime = LocalTime.parse(reservationDetails.getStartTime());

    LocalDate endDay = LocalDate.parse(reservationDetails.getEndDate());
    LocalTime endTime = LocalTime.parse(reservationDetails.getEndTime());

    String eventName = reservationDetails.getResourceName();
    LocalDateTime startDateTime = LocalDateTime.of(startDay, startTime);
    LocalDateTime endDateTime = LocalDateTime.of(endDay, endTime);
    DateTime start = new DateTime(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    DateTime end = new DateTime(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    VEvent meeting = new VEvent(start, end, eventName);

    meeting.getProperties().add(vTimeZone.getTimeZoneId());
    meeting.getProperties().add(new Location(reservationDetails.getResourceName()));

    return setHostInfo(email, meeting);
  }

  private String setHostInfo(String email, VEvent meeting) {
    HostInfo info = null;
    try {
      info = new InetAddressHostInfo(InetAddress.getLocalHost());
    } catch (UnknownHostException e) {
      LOGGER.info(e.getMessage());
    }
    UidGenerator ug = new FixedUidGenerator(info, "" + new SecureRandom().nextInt(Integer.MAX_VALUE));
    Uid uid = ug.generateUid();
    meeting.getProperties().add(uid);

    Attendee dev1 = new Attendee(URI.create("mailto:" + email));
    dev1.getParameters().add(Role.REQ_PARTICIPANT);
    meeting.getProperties().add(dev1);

    return createCalenderOutputter(meeting);
  }

  private String createCalenderOutputter(VEvent meeting) {
    StringWriter fin = new StringWriter();
    net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
    icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
    icsCalendar.getProperties().add(Version.VERSION_2_0);
    icsCalendar.getProperties().add(CalScale.GREGORIAN);
    icsCalendar.getComponents().add(meeting);
    CalendarOutputter calendarOutputter = new CalendarOutputter();
    try {
      calendarOutputter.output(icsCalendar, fin);
    } catch (ValidationException | IOException e) {
      LOGGER.info(e.getMessage());
    }
    return fin.toString();
  }

}
