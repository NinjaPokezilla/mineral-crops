"""
Script para processar texturas:
1. Remove o fundo das imagens (deixa transparente)
2. Renomeia os arquivos removendo o timestamp
3. Redimensiona para 16x16 pixels (padrão Minecraft)
"""

import os
import re
from PIL import Image
from rembg import remove

# Diretório das texturas
TEXTURE_DIR = r"c:\Users\João Vitor\Desktop\addon3\texturas"
OUTPUT_DIR = r"c:\Users\João Vitor\Desktop\addon3\texturas_processadas"

def remove_timestamp(filename):
    """Remove o timestamp do nome do arquivo (ex: _1767121225829)"""
    # Pattern para encontrar _[números].png
    pattern = r'_\d+\.png$'
    new_name = re.sub(pattern, '.png', filename)
    return new_name

def process_texture(input_path, output_path, target_size=(16, 16)):
    """Remove o fundo e redimensiona a textura"""
    print(f"Processando: {os.path.basename(input_path)}")
    
    # Ler a imagem
    with open(input_path, 'rb') as f:
        input_data = f.read()
    
    # Remover o fundo
    output_data = remove(input_data)
    
    # Criar imagem PIL a partir dos bytes
    from io import BytesIO
    img = Image.open(BytesIO(output_data))
    
    # Garantir que está em modo RGBA
    if img.mode != 'RGBA':
        img = img.convert('RGBA')
    
    # Redimensionar para o tamanho alvo
    img_resized = img.resize(target_size, Image.Resampling.LANCZOS)
    
    # Salvar a imagem processada
    img_resized.save(output_path, 'PNG')
    print(f"  -> Salvo: {os.path.basename(output_path)}")

def main():
    # Criar diretório de saída se não existir
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    
    # Listar todos os arquivos PNG no diretório de texturas
    texture_files = [f for f in os.listdir(TEXTURE_DIR) if f.endswith('.png')]
    
    print(f"Encontradas {len(texture_files)} texturas para processar.\n")
    
    for texture_file in texture_files:
        input_path = os.path.join(TEXTURE_DIR, texture_file)
        
        # Remover timestamp do nome
        new_name = remove_timestamp(texture_file)
        output_path = os.path.join(OUTPUT_DIR, new_name)
        
        try:
            process_texture(input_path, output_path)
        except Exception as e:
            print(f"Erro ao processar {texture_file}: {e}")
    
    print(f"\nProcessamento concluído! Texturas salvas em: {OUTPUT_DIR}")

if __name__ == "__main__":
    main()
