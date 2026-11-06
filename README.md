<h1>SuvacoDoSaci — Robocode</h1>

<p>
  Robô desenvolvido em <strong>Java</strong> para a disciplina de
  <strong>Introdução à Computação</strong> do <strong>IFSC</strong>.
</p>

<p>
  <strong>Professor:</strong> Diego Medeiros<br>
  <strong>Integrantes:</strong> Bernardo Zinder, Felipe Menhegel e Guilherme Schosek
</p>

<hr>

<h2>O que o robô faz</h2>

<p>
  O SuvacoDoSaci procura adversários, se aproxima e atira com mais força
  de perto. Usa <code>AdvancedRobot</code> para movimentar o corpo,
  o canhão e o radar ao mesmo tempo.
</p>

<ul>
  <li><strong>Busca:</strong> gira o radar e o canhão em 360°, alternando movimentos para frente e para trás.</li>
  <li><strong>Perseguição:</strong> acompanha o adversário com o radar e avança quando ele está a mais de 120 unidades.</li>
  <li><strong>Disparo:</strong> mira na posição detectada e usa potência 3 a menos de 150 unidades; nas demais distâncias, potência 1. Só tenta atirar com o canhão frio e a mira quase alinhada.</li>
  <li><strong>Reações:</strong> muda de direção ao receber tiros, recua e gira ao bater na parede e tenta atirar antes de recuar ao colidir com outro robô.</li>
</ul>

<br>

<h2>Como executar</h2>

<ol>
  <li>
    Instale o Java e o
    <a href="https://robocode.sourceforge.io/">Robocode</a>.
  </li>
  <li>
    Clone o projeto:
    <pre><code>git clone --branch main https://github.com/schosek/RoboCode-IFSC.git</code></pre>
  </li>
  <li>
    Copie <a href="src/src/SuvacoDoSaci.java">SuvacoDoSaci.java</a>
    para <code>robots/SuvacoDoSaci/</code> dentro da instalação do Robocode.
    Crie a pasta se necessário.
  </li>
  <li>Abra o arquivo no editor do Robocode e compile.</li>
  <li>
    Em <code>Battle &gt; New</code>, selecione o robô e os adversários
    para começar.
  </li>
</ol>

<hr>

<p>
  <a href="https://robocode.sourceforge.io/">Robocode</a> ·
  <a href="https://robowiki.net/">RoboWiki</a>
</p>
