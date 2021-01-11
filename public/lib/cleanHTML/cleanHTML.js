// Clean HTML input string
// Copyright Iacopo Carraro 2016

// Transform tags
var transformTag = [
  ["<ol", "<ul"],
  ["/ol>", "/ul>"]];

// Remove between

var removeFromTo = [
  // remove also the content inside the tags
  ["<script", "/script>"],
  ["<style", "/style>"],
  ["<area", "/area>"],
  ["<audio", "/audio>"],
  ["<applet", "/applet>"],
  ["<canvas", "/canvas>"],
  ["<colgroup", "/colgroup>"],
  ["<datalist", "/datalist>"],
  ["<embed", "/embed>"],
  ["<head", "/head>"],
  ["<map", "/map>"],
  ["<menu", "/menu>"],
  ["<menuitem", "/menuitem>"],
  ["<noscript", "/noscript>"],
  ["<object", "/object>"],
  ["<optgroup", "/optgroup>"],
  ["<option", "/option>"],
  ["<select", "/select>"],
  ["<video", "/video>"],
  // remote the content of the tag only
  ["<!", ">"],
  ["<abbr", ">"],
  ["<acronym", ">"],
  ["<address", ">"],
  ["<area", ">"],
  ["<article", ">"],
  ["<aside", ">"],
  ["<applet", ">"],
  ["<audio", ">"],
  ["<base", ">"],
  ["<basefont", ">"],
  ["<bdi", ">"],
  ["<bdo", ">"],
  ["<big", ">"],
  ["<blockquote", ">"],
  ["<body", ">"],
  ["<button", ">"],
  ["<canvas", ">"],
  ["<caption", ">"],
  ["<center", ">"],
  ["<cite", ">"],
  ["<code", ">"],
  ["<col", ">"],
  ["<colgroup", ">"],
  ["<dd", ">"],
  ["<dfn", ">"],
  ["<datalist", ">"],
  ["<del", ">"],
  ["<details", ">"],
  ["<dialog", ">"],
  ["<div", ">"],
  ["<dl", ">"],
  ["<dt", ">"],
  ["<em", ">"],
  ["<embed", ">"],
  ["<fieldset", ">"],
  ["<figcaption", ">"],
  ["<figure", ">"],
  ["<font", ">"],
  ["<footer", ">"],
  ["<form", ">"],
  ["<frame", ">"],
  ["<frameset", ">"],
  ["<h1", ">"],
  ["<h2", ">"],
  ["<h3", ">"],
  ["<h4", ">"],
  ["<h5", ">"],
  ["<h6", ">"],
  ["<head", ">"],
  ["<header", ">"],
  ["<hr", ">"],
  ["<html", ">"],
  ["<iframe", ">"],
  ["<img", ">"],
  ["<input", ">"],
  ["<ins", ">"],
  ["<kbd", ">"],
  ["<keygen", ">"],
  ["<label", ">"],
  ["<legend", ">"],
  ["<link", ">"],
  ["<main", ">"],
  ["<map", ">"],
  ["<mark", ">"],
  ["<menu", ">"],
  ["<menuitem", ">"],
  ["<meta", ">"],
  ["<meter", ">"],
  ["<nav", ">"],
  ["<noframes", ">"],
  ["<noscript", ">"],
  ["<object", ">"],
  ["<optgroup", ">"],
  ["<option", ">"],
  ["<output", ">"],
  ["<param", ">"],
  ["<pre", ">"],
  ["<progress", ">"],
  ["<q", ">"],
  ["<rp", ">"],
  ["<rt", ">"],
  ["<ruby", ">"],
  ["<s", ">"],
  ["<samp", ">"],
  ["<section", ">"],
  ["<select", ">"],
  ["<small", ">"],
  ["<source", ">"],
  ["<span", ">"],
  ["<strike", ">"],
  ["<strong", ">"],
  ["<sub", ">"],
  ["<summary", ">"],
  ["<sup", ">"],
  ["<table", ">"],
  ["<tbody", ">"],
  ["<td", ">"],
  ["<textarea", ">"],
  ["<tfoot", ">"],
  ["<th", ">"],
  ["<thead", ">"],
  ["<time", ">"],
  ["<title", ">"],
  ["<tr", ">"],
  ["<track", ">"],
  ["<tt", ">"],
  ["<u", ">"],
  ["<var", ">"],
  ["<video", ">"],
  ["<wbr", ">"]];

// Remove all attributes
var removeAttributes = [
  "p",
  "b",
  "i",
  "ul",
  "li",
  "br"];

// Remove all attributes except
// <a href>

function cleanHTML(text){
  text = $("<div/>").html(text).html();
  // Transform tags
  transformTag.forEach(function(v){
    var replacer = new RegExp(v[0], "i");
    text = text.replace(replacer, v[1]);
  });
  // Remove from to
  // For each start string
  removeFromTo.forEach(function(v){
    // if v = "<a" look first for "<a " and then for "<a>" to
    // avoid getting "<area" too for example:
    // so first look for it with a space at the end
    var matcher1 = new RegExp(v[0] + " ", "ig");
    while( (m1 = matcher1.exec(text)) !== null){
      // if i was able to find it
      var subText = text.substring(m1.index);
      // look for the end of it
      var endRegExp = new RegExp(v[1], "i");
      var m2 = endRegExp.exec(subText);
      if (m2 != null) {
        // if i was able to find the end of it
        var endIndex = m1.index + m2.index + v[1].length;
        // remove everything is in the middle
        text = text.substring(0, m1.index) + text.substring(endIndex);
      }
    }
    // second look for it with a > closing the tag
    matcher1 = new RegExp(v[0] + ">", "ig");
    while( (m1 = matcher1.exec(text)) !== null){
      // if it was found look for the closing tag
      var subText = text.substring(m1.index);
      var endRegExp = new RegExp(v[1], "i");
      var m2 = endRegExp.exec(subText);
      if (m2 != null) {
        // if the closing tag was found
        var endIndex = m1.index + m2.index + v[1].length;
        // cut everything is in the middle of the 2
        text = text.substring(0, m1.index) + text.substring(endIndex);
      }
    }
    // if the clsing tag is > it means we need to look for </TAG> closing and also remove it
    if ( v[1] == ">" ){
      // Remove also from </tag to > if v[1] was >
      var closingTag = "</" + v[0].substring(1) +  ">";
      var s = new RegExp(closingTag, "ig");
      while( (m1 = s.exec(text)) !== null) {
        text = text.substring(0, m1.index) + text.substring(m1.index + closingTag.length);
      }
    }
  });

  removeAttributes.forEach(function(v){
    var lookStr = "<" + v + " ";
    var r = new RegExp(lookStr, "ig");
    while ( (m1 = r.exec(text)) != null) {
      var endRegEx = new RegExp(">", "i");
      var m2 = endRegEx.exec(text.substring(m1.index + lookStr.length - 1));
      if (m2 != null) {
        text = text.substring(0, m1.index + lookStr.length - 1) + text.substring(m1.index + lookStr.length - 1 + m2.index);
      }
    }
  });

  // Remove all attributes axcept for href for Anchor tags:
  var r = new RegExp("<a ", "ig");
  while ( (m1 = r.exec(text)) != null) {
    var endRegEx = new RegExp(">", "i");
    var m2 = endRegEx.exec(text.substring(m1.index + 2));
    if (m2 != null) {
      middle = text.substring(m1.index + 3, m1.index + 3 + m2.index);
      if ( middle.indexOf("href") != -1 ){
        // we have an href attribute, let's save it
        var aStr = text.substr(m1.index, m2.index + 1);
        var hrefValue
        aStr.split(" ").forEach(function(s){
          if (s.toLowerCase().startsWith("href")) {
            var parts = s.split("=");
            hrefValue = parts[1];
          }
        });
        text = text.substring(0, m1.index) + "<a target=\"_blank\" href=" + hrefValue + ">" + text.substring(m1.index + 3 + m2.index);
      } else {
        // no href attribute, cut all the attributes
        text = text.substring(0, m1.index) + "<a>" + text.substring(m1.index + 3 + m2.index);
      }
    }
  }
  return text;
}

function replaceSelectionWithHtml(html) {
    var range;
    if (window.getSelection && window.getSelection().getRangeAt) {
        range = window.getSelection().getRangeAt(0);
        range.deleteContents();
        var div = document.createElement("p");
        div.innerHTML = html;
        var frag = document.createDocumentFragment(), child;
        while ( (child = div.firstChild) ) {
            frag.appendChild(child);
        }
        range.insertNode(frag);
    } else if (document.selection && document.selection.createRange) {
        range = document.selection.createRange();
        range.pasteHTML(html);
    }
}

window.pasteHandled = {};

function didPaste(e){
  // If the current paste event wasn't already handled:
  if (!(e.timeStamp in pasteHandled)){
    // avoid to handle again the same paste event
    pasteHandled[e.timeStamp] = true;
    e.stopPropagation();
    // check if the paste is of plain text or not:
    var clipboardData = e.clipboardData || window.clipboardData;
    // get the rich text to paste and fall back to the plain or default paste text
    var pastedData = clipboardData.getData('text/html') || clipboardData.getData('text/plain') || clipboardData.getData('Text');
    // get the plain or default paste text
    var plainTextData = clipboardData.getData('text/plain') || clipboardData.getData('Text');
    // if the rich text has the same length of the plain text let the browser perform the paste
    if (pastedData.length != plainTextData.length){
      // Prevent normal paste behavior
      e.preventDefault();
      // Get the paste data
      var cleanedData;
      // Clean the text with our function
      cleanedData = cleanHTML(pastedData);
      // Replace the current selected text with our text
      replaceSelectionWithHtml(cleanedData);
    }
  }
}

function recursiveAttachPasteListener(el, cb){
  // Attach the didPaste function with the passed cb to call both when the paste event happen
  var pasteCb = function(e) {didPaste(e); if(typeof cb === "function"){cb();}};
  el.removeEventListener("paste", pasteCb);
  el.addEventListener("paste", pasteCb);
  if (el.hasChildNodes()){
    var allChildren = Array.prototype.slice.call(el.childNodes);
    if (allChildren.length > 0){
      allChildren.forEach(function(el_child){
        recursiveAttachPasteListener(el_child, cb);
      });
    }
  }
}

function replaceSelectedText(replacementText) {
    var sel, range;
    if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount) {
            range = sel.getRangeAt(0);
            range.deleteContents();
            range.insertNode(document.createTextNode(replacementText));
        }
    } else if (document.selection && document.selection.createRange) {
        range = document.selection.createRange();
        range.text = replacementText;
    }
}