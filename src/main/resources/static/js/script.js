document.getElementById('uploadForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);

    fetch('/students/uploadFace', {
        method: 'POST',
        body: formData
    })
        .then(response => response.text())
        .then(data => {
            const resultDiv = document.getElementById('result');
            resultDiv.innerHTML = `<p>Face Feature: ${data}</p>`;
            resultDiv.className = 'success';

            setTimeout(() => {
                resultDiv.innerHTML = '';
                resultDiv.className = '';
            }, 5000);
        })
        .catch(error => {
            const resultDiv = document.getElementById('result');
            resultDiv.innerHTML = '上传失败，请检查网络或图片格式';
            resultDiv.className = 'error';
        });
});
