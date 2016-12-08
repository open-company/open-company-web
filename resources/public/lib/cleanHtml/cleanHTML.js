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
  // Transform tags
  transformTag.forEach(function(v){
    var replacer = new RegExp(v[0], "i");
    text = text.replace(replacer, v[1]);
  });
  // Remove from to
  removeFromTo.forEach(function(v){
    var matcher1 = new RegExp(v[0] + " ", "ig");
    while( (m1 = matcher1.exec(text)) != null){
      var subText = text.substring(m1.index);
      var endRegExp = new RegExp(v[1], "i");
      var m2 = endRegExp.exec(subText);
      if (m2 != null) {
        var endIndex = m1.index + m2.index + v[1].length;
        text = text.substring(0, m1.index) + text.substring(endIndex);
      }
    }
    matcher1 = new RegExp(v[0] + ">", "ig");
    while( (m1 = matcher1.exec(text)) != null){
      var subText = text.substring(m1.index);
      var endRegExp = new RegExp(v[1], "i");
      var m2 = endRegExp.exec(subText);
      if (m2 != null) {
        var endIndex = m1.index + m2.index + v[1].length;
        text = text.substring(0, m1.index) + text.substring(endIndex);
      }
    }

    if (v[1] = ">"){
      // Remove also from </tag to > if v[1] was >
      var closingTag = "</" + v[0].substring(1) +  ">";
      var s = new RegExp(closingTag, "ig");
      while( (m1 = s.exec(text)) != null) {
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
  return text;
}

function replaceSelectionWithHtml(html) {
    var range;
    if (window.getSelection && window.getSelection().getRangeAt) {
        console.log("replaceSelectionWithHtml 1");
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
        console.log("replaceSelectionWithHtml 2");
        range = document.selection.createRange();
        range.pasteHTML(html);
    }
}

function cleanHTMLOnElement(el) {
  el.addEventListener("paste",
    function(e){
      console.log("Paste", cleanHTML(e.clipboardData.getData("text/html")));
      replaceSelectionWithHtml(cleanHTML(e.clipboardData.getData("text/html")));
      // Stop data actually being pasted into div
      e.stopPropagation();
      e.preventDefault();
    }
  );
}