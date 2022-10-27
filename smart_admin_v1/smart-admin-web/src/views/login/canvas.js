// 离子波浪
export const lonWave = () => {
  var starlings = function (n, r, t, o, e, u, i, f) {
    var a = f.onSetup
    void 0 === a && (a = null)
    var v = f.onRepeat
    void 0 === v && (v = null)
    var c = f.modifier
    void 0 === c && (c = null)
    var l = f.perspective
    void 0 === l && (l = 1)
    var d = f.pixelRatio
    void 0 === d && (d = 1)
    var m = f.triangles
    void 0 === m && (m = !1)
    var s
    var p
    var y = r.length
    var w = function (n, r) {
      let t = s.createShader(n)
      return s.shaderSource(t, r), s.compileShader(t), t
    }
    var b = function () {
      for (var n = 0; n < o.length; n += 1) {
        for (var r = s.createBuffer(), e = o[n], u = e.data(0, 0).length, i = new Float32Array(t * y * u), f = 0; f < t; f += 1) {
          for (var a = e.data(f, t), v = f * y * u, l = 0; l < y; l += 1) {
            for (var d = 0; d < u; d += 1) {
              c !== null && e.name === c.attribute ? i[v] = c.value(i[v], a, d, l) : i[v] = a[d]
              v += 1
            }
          }
        }
        s.bindBuffer(s.ARRAY_BUFFER, r)
        s.bufferData(s.ARRAY_BUFFER, i, s.STATIC_DRAW)
        var m = s.getAttribLocation(p, o[n].name)
        s.enableVertexAttribArray(m)
        s.vertexAttribPointer(m, u, s.FLOAT, !1, !1, 0, 0)
      }
    }
    var A = function () {
      e.push({
        name: 'uMVP',
        type: 'mat4'
      })
      for (var n = 0; n < e.length; n += 1) {
        var r = s.getUniformLocation(p, e[n].name)
        e[n].location = r
      }
    }
    var F = {
      float: function (n, r) {
        return s.uniform1f(n, r)
      },
      vec2: function (n, r) {
        return s.uniform2fv(n, r)
      },
      vec3: function (n, r) {
        return s.uniform3fv(n, r)
      },
      vec4: function (n, r) {
        return s.uniform4fv(n, r)
      },
      mat2: function (n, r) {
        return s.uniformMatrix2fv(n, !1, r)
      },
      mat3: function (n, r) {
        return s.uniformMatrix3fv(n, !1, r)
      },
      mat4: function (n, r) {
        return s.uniformMatrix4fv(n, !1, r)
      }
    }
    var g = function () {
      s.clear(16640)
      s.useProgram(p)
      v !== null && v(s, p, e)
      for (var n = 0; n < e.length; n += 1) F[e[n].type](e[n].location, e[n].value)
      s.drawArrays(m ? s.TRIANGLES : s.POINTS, 0, y * t)
      requestAnimationFrame(g)
    }
    var h = function () {
      n.width = n.clientWidth * d
      n.height = n.clientHeight * d
      var r = s.drawingBufferWidth
      var t = s.drawingBufferHeight
      s.viewport(0, 0, r, t)
      e[e.length - 1].value = [l / (r / t), 0, 0, 0, 0, l, 0, 0, 0, 0, -1, -1, 0, 0, 1, 1]
    }
    s = n.getContext('webgl')
    p = s.createProgram()
    s.attachShader(p, w(s.VERTEX_SHADER, u))
    s.attachShader(p, w(s.FRAGMENT_SHADER, i))
    s.linkProgram(p)
    A()
    h()
    b()
    a !== null && a(s)
    g()
    window.addEventListener('resize', h, !1)
  }

  // Do you like rainbow waves?
  var rainbow = false

  // Need more performance?
  var HD = true

  var canvas = document.getElementById('canvas')
  var background = document.querySelector('.background')
  var bar = document.querySelector('.progress')
  var initialize = function initialize (vertices) {
    var pixelRatio = HD ? window.devicePixelRatio : 1
    var rows = HD ? 90 : 90
    var multiplier = rows * rows
    var duration = 0.4
    var geometry = [{
      x: 0,
      y: 0,
      z: 0
    }]
    var pointSize = (HD ? 6 : 2).toFixed(1)

    var step = 0.004
    var size = 5
    var attributes = [{
      name: 'aPositionStart',
      data: function data (i, total) {
        return [size - (i % rows / rows + 0.5 / rows) * (size * 2), -1, (size - (Math.floor(i / rows) / rows + 0.5 / rows) * size * 2) * -1]
      }
    },
    {
      name: 'aControlPointOne',
      data: function data (i) {
        return [size - (i % rows / rows + 0.5 / rows) * (size * 2), -0.5 + getRandom(0.2), (size - (Math.floor(i / rows) / rows + 0.5 / rows) * size * 2) * -1]
      }
    },
    {
      name: 'aControlPointTwo',
      data: function data (i) {
        return [size - (i % rows / rows + 0.5 / rows) * (size * 2), -0.5 + getRandom(0.2), (size - (Math.floor(i / rows) / rows + 0.5 / rows) * size * 2) * -1]
      }
    },
    {
      name: 'aPositionEnd',
      data: function data (i) {
        return [size - (i % rows / rows + 0.5 / rows) * (size * 2), -1, (size - (Math.floor(i / rows) / rows + 0.5 / rows) * size * 2) * -1]
      }
    },
    {
      name: 'aOffset',
      data: function data (i) {
        return [i * ((1 - duration) / (multiplier - 1))]
      }
    },
    {
      name: 'aColor',
      data: function data (i, total) {
        return getHSL(rainbow ? i / total * 1.0 : 0.5 + i / total * 0.4, 0.5, 0.5)
      }
    }]

    var uniforms = [{
      name: 'uProgress',
      type: 'float',
      value: 0.8
    }]

    var vertexShader = '\n  attribute vec3 aPositionStart;\n  attribute vec3 aControlPointOne;\n  attribute vec3 aControlPointTwo;\n  attribute vec3 aPositionEnd;\n  attribute float aOffset;\n  attribute vec3 aColor;\n\n  uniform float uProgress;\n  uniform mat4 uMVP;\n\n  varying vec3 vColor;\n\n  vec3 bezier4(vec3 a, vec3 b, vec3 c, vec3 d, float t) {\n    return mix(mix(mix(a, b, t), mix(b, c, t), t), mix(mix(b, c, t), mix(c, d, t), t), t);\n  }\n\n  float easeInOutQuint(float t){\n    return t < 0.5 ? 16.0 * t * t * t * t * t : 1.0 + 16.0 * (--t) * t * t * t * t;\n  }\n\n  void main () {\n    float tProgress = easeInOutQuint(min(1.0, max(0.0, (uProgress - aOffset)) / ' + duration + '));\n    vec3 newPosition = bezier4(aPositionStart, aControlPointOne, aControlPointTwo, aPositionEnd, tProgress);\n    gl_PointSize = ' + pointSize + ' + ((newPosition.y + 1.0) * 80.0);\n    gl_Position = uMVP * vec4(newPosition, 1.0);\n    vColor = aColor;\n  }\n'

    var fragmentShader = '\n  precision mediump float;\n\n  varying vec3 vColor;\n\n  void main() {\n     vec2 pc = 2.0 * gl_PointCoord - 1.0;\n     gl_FragColor = vec4(vColor, 1.0 - dot(pc, pc));\n  }\n'

    var onSetup = function onSetup (gl) {
      gl.blendFunc(gl.SRC_ALPHA, gl.ONE)
      gl.enable(gl.BLEND)
    }

    var onRepeat = function onRepeat () {
      rotateY(uniforms[uniforms.length - 1].value, 0.002)
      if (uniforms[0].value < 0) {
        uniforms[0].value = 1
      }
      uniforms[0].value -= step
    }

    var options = {
      onSetup: onSetup,
      onRepeat: onRepeat,
      pixelRatio: pixelRatio
    }

    starlings(canvas, geometry, multiplier, attributes, uniforms, vertexShader, fragmentShader, options)
  }

  var getRandom = function getRandom (value) {
    return Math.random() * value - value / 2
  }

  var rotateY = function rotateY (matrix, angle) {
    var sin = Math.sin(angle)
    var cos = Math.cos(angle)
    var clone = JSON.parse(JSON.stringify(matrix))

    matrix[0] = clone[0] * cos - clone[8] * sin
    matrix[1] = clone[1] * cos - clone[9] * sin
    matrix[2] = clone[2] * cos - clone[10] * sin
    matrix[3] = clone[3] * cos - clone[11] * sin
    matrix[8] = clone[0] * sin + clone[8] * cos
    matrix[9] = clone[1] * sin + clone[9] * cos
    matrix[10] = clone[2] * sin + clone[10] * cos
    matrix[11] = clone[3] * sin + clone[11] * cos
  }

  var h2r = function h2r (p, q, t) {
    if (t < 0) t += 1
    if (t > 1) t -= 1
    if (t < 1 / 6) return p + (q - p) * 6 * t
    if (t < 1 / 2) return q
    if (t < 2 / 3) return p + (q - p) * 6 * (2 / 3 - t)
    return p
  }

  var getHSL = function getHSL (h, s, l) {
    h = (h % 1 + 1) % 1
    s = Math.max(0, Math.min(1, s))
    l = Math.max(0, Math.min(1, l))
    if (s === 0) return [l, l, l]
    var p = l <= 0.5 ? l * (1 + s) : l + s - l * s
    var q = 2 * l - p
    return [h2r(q, p, h + 1 / 3), h2r(q, p, h), h2r(q, p, h - 1 / 3)]
  }
  initialize()
}

// 随机线条
export const canvasParticle = (function () {
  function getElementByTag (name) { return document.getElementsByTagName(name) }
  function getELementById (id) { return document.getElementById(id) }
  function canvasInit (canvasConfig) {
    canvasConfig = canvasConfig || {}
    var html = getElementByTag('html')[0]
    var body = document.getElementById('canvasView')
    var canvasObj = document.createElement('canvas')
    var canvas = { element: canvasObj, points: [], config: { vx: canvasConfig.vx || 4, vy: canvasConfig.vy || 4, height: canvasConfig.height || 2, width: canvasConfig.width || 2, count: canvasConfig.count || 100, color: canvasConfig.color || '121, 162, 185', stroke: canvasConfig.stroke || '130,255,255', dist: canvasConfig.dist || 6000, e_dist: canvasConfig.e_dist || 20000, max_conn: 10 } }; if (canvas.element.getContext('2d')) { canvas.context = canvas.element.getContext('2d') } else { return null }
    body.style.padding = '0'; body.style.margin = '0'; body.appendChild(canvas.element); canvas.element.style = 'position: fixed; top: 0; left: 0; z-index: -1;'; canvasSize(canvas.element); window.onresize = function () { canvasSize(canvas.element) }
    body.onmousemove = function (e) { var event = e || window.event; canvas.mouse = { x: event.clientX, y: event.clientY } }
    document.onmouseleave = function () { canvas.mouse = undefined }
    setInterval(function () { drawPoint(canvas) }, 40)
  }
  function canvasSize (canvas) {
    var width = document.getElementById('canvasView').style.width
    var height = document.getElementById('canvasView').style.height
    width = parseInt(width); height = parseInt(height)
    canvas.width = width || window.innerWeight || document.documentElement.clientWidth || document.body.clientWidth; canvas.height = height || window.innerWeight || document.documentElement.clientHeight || document.body.clientHeight
  }
  function drawPoint (canvas) {
    var context = canvas.context
    var point
    var dist
    context.clearRect(0, 0, canvas.element.width, canvas.element.height); context.beginPath(); context.fillStyle = 'rgb(' + canvas.config.color + ')'; for (var i = 0, len = canvas.config.count; i < len; i++) {
      if (canvas.points.length != canvas.config.count) { point = { x: Math.floor(Math.random() * canvas.element.width), y: Math.floor(Math.random() * canvas.element.height), vx: canvas.config.vx / 2 - Math.random() * canvas.config.vx, vy: canvas.config.vy / 2 - Math.random() * canvas.config.vy } } else { point = borderPoint(canvas.points[i], canvas) }
      context.fillRect(point.x - canvas.config.width / 2, point.y - canvas.config.height / 2, canvas.config.width, canvas.config.height); canvas.points[i] = point
    }
    drawLine(context, canvas, canvas.mouse); context.closePath()
  }
  function borderPoint (point, canvas) {
    var p = point; if (point.x <= 0 || point.x >= canvas.element.width) { p.vx = -p.vx; p.x += p.vx } else if (point.y <= 0 || point.y >= canvas.element.height) { p.vy = -p.vy; p.y += p.vy } else { p = { x: p.x + p.vx, y: p.y + p.vy, vx: p.vx, vy: p.vy } }
    return p
  }
  function drawLine (context, canvas, mouse) {
    let dist
    context = context || canvas.context; for (var i = 0, len = canvas.config.count; i < len; i++) {
      canvas.points[i].max_conn = 0; for (var j = 0; j < len; j++) {
        if (i != j) {
          dist = Math.round(canvas.points[i].x - canvas.points[j].x) * Math.round(canvas.points[i].x - canvas.points[j].x) +
            Math.round(canvas.points[i].y - canvas.points[j].y) * Math.round(canvas.points[i].y - canvas.points[j].y); if (dist <= canvas.config.dist && canvas.points[i].max_conn < canvas.config.max_conn) {
              canvas.points[i].max_conn++; context.lineWidth = 0.5 - dist / canvas.config.dist; context.strokeStyle = 'rgba(' + canvas.config.stroke + ',' + (1 - dist / canvas.config.dist) + ')'
              context.beginPath(); context.moveTo(canvas.points[i].x, canvas.points[i].y); context.lineTo(canvas.points[j].x, canvas.points[j].y); context.stroke()
            }
        }
      }
      if (mouse) {
        dist = Math.round(canvas.points[i].x - mouse.x) * Math.round(canvas.points[i].x - mouse.x) +
          Math.round(canvas.points[i].y - mouse.y) * Math.round(canvas.points[i].y - mouse.y); if (dist > canvas.config.dist && dist <= canvas.config.e_dist) { canvas.points[i].x = canvas.points[i].x + (mouse.x - canvas.points[i].x) / 20; canvas.points[i].y = canvas.points[i].y + (mouse.y - canvas.points[i].y) / 20 }
        if (dist <= canvas.config.e_dist) { context.lineWidth = 1; context.strokeStyle = 'rgba(' + canvas.config.stroke + ',' + (1 - dist / canvas.config.e_dist) + ')'; context.beginPath(); context.moveTo(canvas.points[i].x, canvas.points[i].y); context.lineTo(mouse.x, mouse.y); context.stroke() }
      }
    }
  }
  return canvasInit
})()
