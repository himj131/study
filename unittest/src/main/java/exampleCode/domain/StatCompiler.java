/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
***/
package exampleCode.domain;

import java.util.*;
import java.util.concurrent.atomic.*;

public class StatCompiler {
   static Question q1 = new BooleanQuestion("Tuition reimbursement?");
   static Question q2 = new BooleanQuestion("Relocation package?");

   class QuestionController {
      Question find(int id) {
         if (id == 1)
            return q1;
         else
            return q2;
      }
   }

   private QuestionController controller = new QuestionController();

   public Map<Integer,String> questionText(List<BooleanAnswer> answers) {
      Map<Integer,String> questions = new HashMap<>();
      answers.stream().forEach(answer -> {
         if (!questions.containsKey(answer.getQuestionId()))
            questions.put(answer.getQuestionId(),
                controller.find(answer.getQuestionId()).getText()); });
      return questions;
   }


   public Map<String, Map<Boolean, AtomicInteger>> responsesByQuestion(
       List<BooleanAnswer> answers) {
      Map<Integer, Map<Boolean, AtomicInteger>> responses = new HashMap<>();
      answers.forEach(answer -> incrementHistogram(responses, answer));
      return convertHistogramIdsToText(responses);
   }

   public Map<String, Map<Boolean, AtomicInteger>> responsesByQuestion(
       List<BooleanAnswer> answers, Map<Integer, String> questions) {
      Map<Integer, Map<Boolean, AtomicInteger>> responses = new HashMap<>();
      answers.forEach(answer -> incrementHistogram(responses, answer));
      return convertHistogramIdsToText(responses, questions);
   }

   //before
   private Map<String, Map<Boolean, AtomicInteger>> convertHistogramIdsToText(
         Map<Integer, Map<Boolean, AtomicInteger>> responses) {
      Map<String, Map<Boolean, AtomicInteger>> textResponses = new HashMap<>();
      responses.keySet().forEach(id ->
         textResponses.put(controller.find(id).getText(), responses.get(id)));  // db 조회 -> 느림
      return textResponses;
   }

   //after
   private Map<String, Map<Boolean, AtomicInteger>> convertHistogramIdsToText(
       Map<Integer, Map<Boolean, AtomicInteger>> responses, Map<Integer, String> questions) {
      Map<String, Map<Boolean, AtomicInteger>> textResponses = new HashMap<>();
      responses.keySet().forEach(id ->
          textResponses.put(questions.get(id), responses.get(id)));
      return textResponses;
   }

   private void incrementHistogram(
         Map<Integer, Map<Boolean, AtomicInteger>> responses, 
         BooleanAnswer answer) {
      Map<Boolean, AtomicInteger> histogram = 
            getHistogram(responses, answer.getQuestionId());
      histogram.get(answer.getValue()).getAndIncrement();
   }

   private Map<Boolean, AtomicInteger> getHistogram(
         Map<Integer, Map<Boolean, AtomicInteger>> responses, int id) {
      Map<Boolean, AtomicInteger> histogram;
      if (responses.containsKey(id)) 
         histogram = responses.get(id);
      else {
         histogram = createNewHistogram();
         responses.put(id, histogram);
      }
      return histogram;
   }

   private Map<Boolean, AtomicInteger> createNewHistogram() {
      Map<Boolean, AtomicInteger> histogram;
      histogram = new HashMap<>();
      histogram.put(Boolean.FALSE, new AtomicInteger(0));
      histogram.put(Boolean.TRUE, new AtomicInteger(0));
      return histogram;
   }
}
