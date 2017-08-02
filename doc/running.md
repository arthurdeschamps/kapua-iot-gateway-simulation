<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Run</title>
<link rel="stylesheet" href="https://stackedit.io/res-min/themes/base.css" />
<script type="text/javascript" src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS_HTML"></script>
</head>
<body><div class="container"><h1 id="running-the-simulation">Running the simulation</h1>



<h2 id="running-the-kapua-services">Running the <strong>Kapua</strong> services</h2>

<p>If you haven’t already, take a look at <a href="https://github.com/eclipse/kapua/tree/develop/assembly">these instructions</a>. <br>
You will need at least 3 Kapua services started in order to run the simulation:</p>

<ol>
<li>kapua-broker</li>
<li>kapua-sql</li>
<li>kapua-elasticsearch</li>
</ol>

<p>If you have the Kapua docker containers created on your computer, you can start them with:</p>

<pre><code>docker start kapua-sql
docker start kapua-elasticsearch
docker start kapua-broker
</code></pre>



<h2 id="running-simulator-and-data-transmitter">Running <strong>simulator</strong> and <strong>data-transmitter</strong></h2>

<ul>
<li><p><strong>The maven way</strong> <br>
You will need Maven installed. See <a href="https://maven.apache.org/install.html">these</a> instructions. <br>
Go to <strong>simulator</strong> directory and run:</p>

<pre><code>$ mvn exec:java
</code></pre>

<p>Go to <strong>data-transmitter</strong> directory and run:</p>

<pre><code>$ mvn exec:java
</code></pre>

<p>That’s it !</p></li>
<li><p><strong>The IDE way</strong> <br>
If you have access to <strong>IntelliJ idea</strong> (or similar), you can import the global project in the IDE and simply run the two modules <strong>simulator</strong> and <strong>data-transmitter</strong> (no matter the order). No specific option is required, just run the main class for both module.</p></li>
<li><p><strong>The JAR way</strong> <br>
Go to directory <strong>simulator/target</strong> and run:</p>

<pre><code>$ java -jar simulator-1.0-SNAPSHOT-jar-with-dependencies.jar
</code></pre>

<p>Go to directory <strong>data-transmitter/target</strong> and run:</p>

<pre><code>$ java -jar data-transmitter-1.0-SNAPSHOT-jar-with-dependencies.jar 
</code></pre></li>
</ul>



<h2 id="running-the-web-application">Running the <strong>web application</strong></h2>

<ul>
<li><p><strong>The easy way</strong> <br>
Go to <strong>webapp-angular/build/web/</strong> and open index.html in any browser you desire.</p></li>
<li><p><strong>The pub way</strong></p>

<ol><li><p>Install the <strong>pub</strong> tool. Follow this <a href="https://www.dartlang.org/tools/pub/installing">link</a> for further instructions.</p></li>
<li><p>Then, if you don’t already have it, install <a href="https://webdev.dartlang.org/tools/dartium">Dartium</a>  (Chromium with the Dart VM). </p></li>
<li><p>Thereafter, run in <strong>webapp-angular</strong> directory:</p>

<pre><code>$ pub serve --port 8082
</code></pre>

<p>and open localhost:8082 in Dartium.</p>

<p><em>Note that you can choose another port, but neither 8080 or 8081 will be available if you have <strong>kapua-console</strong> and <strong>kapua-api</strong> running on their default ports.</em></p></li></ol></li>
<li><p><strong>The IDE way</strong> <br>
Using <strong>WebStorm</strong> you can simply run the built-in configuration called “index.html”. <br>
If you encounter 404 errors, just change the port of the configuration. For instance change it from 63342 to 63343.</p></li>
</ul></div></body>
</html>