const baseUrl = 'http://localhost:8080';
const appEl = document.getElementById('app');

async function hashHandler() {
  const hash = !location.hash ? '#/' : location.hash;
  appEl.innerHTML = await routes[hash]();
}

function buildTemplate(tmpId, context) {
  var template = $('#' + tmpId).html();
  var templateScript = Handlebars.compile(template);
  var html = templateScript(context);
  return html;
}

const routes = {
  '#/': async() => {
    const res = await fetch(`${baseUrl}/v1/buckets`)
    const json = await res.json();
    return buildTemplate('buckets-list', { buckets: json })
  }
}

hashHandler();

window.addEventListener('hashchange', hashHandler, false);
