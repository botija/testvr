#!BPY

import struct


"""
Name: '3DBT Exporter'
Blender: 277
Group: 'Export'
Tooltip: '3DBT Exporter'
"""

bl_info = {
    "name": "DBT Exporter",
    "category": "Export",
}

import bpy


def writechar(file, c):
  #file.write("%c" % ord(c))
  #file.write('>B', c )
  file.write( c )

def writefloat(file, f):
  file.write(struct.pack('>f', f))

def writeint(file, i):
  file.write(struct.pack('>i', i))

def writeshort(file, s):
  file.write(struct.pack('>h', s))

def writelong(file, l):
  file.write(struct.pack('>l', l))


class EmptyUV:
  uv = (0.0, 0.0)
  def __getitem__(self, index): return self
  
class EmptyColor:
  color = (1.0, 1.0, 1.0)
  def __getitem__(self, index): return self
  
class Submesh:
  def __init__(self, model_name):
    self.mesh = bpy.data.meshes[model_name]
    self.data = bpy.data.objects[model_name]
	
  def export(self, out):
    uv_act = self.mesh.uv_layers.active
    uv_layer = uv_act.data if (uv_act is not None) else EmptyUV()
    #color_data = self.mesh.vertex_colors[0].data
	
    use_color = True
    if ( (len(self.mesh.vertex_colors) > 0) and self.mesh.vertex_colors[0].data is not None and len(self.mesh.vertex_colors[0].data)>0):
      color_data = self.mesh.vertex_colors.active.data
    else:
      use_color = False
	   
    verts = self.mesh.vertices

    #name and texture
    writechar(out, bytes((len(self.mesh.name),)))
    for i in self.mesh.name:
       writechar(out, bytes((ord(i),)))
	#texture filename???
    writechar(out, bytes((len(self.mesh.name),)))
    for i in self.mesh.name:
       writechar(out, bytes((ord(i),)))

    padding = 4 - (len(self.mesh.name)+len(self.mesh.name)+2+1) % 4
    writechar(out, bytes((padding,)))
    for i in range(padding):
       writechar(out, b'\x00')
	
	
    writeint(out,len(self.mesh.polygons)*3)  #vertex
    writeint(out,len(self.mesh.polygons)*3)  #normals
    writeint(out,len(self.mesh.polygons)*3)  #uv
    if (use_color):
      writeint(out,len(self.mesh.polygons)*3)  #color
    else:
      writeint(out, 0)  #color
	  
    writeint(out,len(self.mesh.polygons)*3)  #indexes
	
    loop_vert = {l.index: l.vertex_index for l in self.mesh.loops}
    for face in self.mesh.polygons:
        for li in face.loop_indices:
            #print("Normal:", *verts[loop_vert[li]].normal)
            #print("Position:", *verts[loop_vert[li]].co)
            #print("UV:", *uv_layer[li].uv)
            #print("Color:", *color_data[li].color)
            co = verts[loop_vert[li]].co
            writefloat(out, float(co.x))
            writefloat(out, float(co.y))
            writefloat(out, float(co.z))
			
    for face in self.mesh.polygons:
        for li in face.loop_indices:			
            normal = verts[loop_vert[li]].normal
            writefloat(out, float(normal.x))
            writefloat(out, float(normal.y))
            writefloat(out, float(normal.z))
			

			
    for face in self.mesh.polygons:
        for li in face.loop_indices:
            uv = uv_layer[li].uv
            writefloat(out, float(uv.x))
            writefloat(out, float(uv.y))
	
    if (use_color):	
      for face in self.mesh.polygons:
          for li in face.loop_indices:			
            color = color_data[li].color
            writechar(out, bytes((int(color[0]*255),)))
            writechar(out, bytes((int(color[1]*255),)))
            writechar(out, bytes((int(color[2]*255),)))
            #writechar(out, bytes((255,)))
            #writechar(out, bytes((255,)))
            #writechar(out, bytes((255,)))
			

    for face in self.mesh.polygons:
        for li in face.loop_indices:		
            writeshort(out, li)		
			

			
class Mesh:
  def __init__(self):
     pass
	 
  def export(self, filename):	 
    self.out = open(filename,"wb")
	#Header
    writechar(self.out, b'3') #Magic
    writechar(self.out, b'D')
    writechar(self.out, b'B')
    writechar(self.out, b'T')
    writechar(self.out, b'\x00') #version major, minor
    writechar(self.out, b'\x01')
	
    bpy.ops.object.mode_set(mode = 'OBJECT')
	
    writeshort(self.out, len(bpy.data.meshes))   #number of meshes
    for m in bpy.data.meshes:
        submesh = Submesh(m.name)
        submesh.export(self.out)
	  
    self.out.close()
	
def write_obj(filepath):
    m = Mesh()
    m.export(filepath)
	  

if __name__ == "__main__":
    write_obj("model.3dbt")	
    print("Model exported.")	
    bpy.ops.wm.quit_blender()
#Blender.Window.FileSelector(write_obj, "Export")
