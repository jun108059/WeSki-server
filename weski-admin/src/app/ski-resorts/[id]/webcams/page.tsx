'use client'

import React, { useState, useEffect, useCallback } from 'react'
import { useParams, useRouter } from 'next/navigation'
import {
    Table,
    Button,
    Modal,
    Form,
    Input,
    message,
    Breadcrumb,
    Typography,
    Card,
    Spin,
} from 'antd'
import { ArrowLeftOutlined, EditOutlined, VideoCameraOutlined } from '@ant-design/icons'
import { skiResortApi } from '@/api/skiResortApi'
import type { Webcam } from '@/types/skiResort'

const { Title } = Typography

export default function WebcamListPage() {
    const params = useParams()
    const router = useRouter()
    const [form] = Form.useForm()
    const [loading, setLoading] = useState(false)
    const [webcams, setWebcams] = useState<Webcam[]>([])
    const [isModalVisible, setIsModalVisible] = useState(false)
    const [editingWebcam, setEditingWebcam] = useState<Webcam | null>(null)
    const [resortName, setResortName] = useState('')

    const id = params?.id as string

    const loadData = useCallback(async () => {
        if (!id) return
        setLoading(true)
        try {
            // 스키장 정보도 가져와서 이름 표시
            const [resortData, webcamsData] = await Promise.all([
                skiResortApi.getSkiResort(Number(id)),
                skiResortApi.getWebcams(Number(id)),
            ])
            setResortName(resortData.name)
            setWebcams(webcamsData)
        } catch (error) {
            console.error('데이터 로드 실패:', error)
            message.error('데이터를 불러오는데 실패했습니다')
        } finally {
            setLoading(false)
        }
    }, [id])

    useEffect(() => {
        loadData()
    }, [loadData])

    const showEditModal = (webcam: Webcam) => {
        setEditingWebcam(webcam)
        form.setFieldsValue({
            name: webcam.name,
            description: webcam.description,
            url: webcam.url,
        })
        setIsModalVisible(true)
    }

    const handleUpdate = async () => {
        if (!editingWebcam) return
        try {
            const values = await form.validateFields()
            await skiResortApi.updateWebcam(editingWebcam.id, values)
            message.success('웹캠 정보가 수정되었습니다')
            setIsModalVisible(false)
            form.resetFields()
            loadData() // 목록 새로고침
        } catch (error) {
            console.error('웹캠 수정 실패:', error)
            message.error('웹캠 수정에 실패했습니다')
        }
    }

    const columns = [
        { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
        { title: '번호', dataIndex: 'number', key: 'number', width: 80 },
        { title: '이름', dataIndex: 'name', key: 'name' },
        { title: '설명', dataIndex: 'description', key: 'description' },
        {
            title: 'URL',
            dataIndex: 'url',
            key: 'url',
            render: (url?: string) => url ? (
                <a href={url} target="_blank" rel="noopener noreferrer" style={{ maxWidth: 300, display: 'inline-block', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {url}
                </a>
            ) : <span style={{ color: '#999' }}>-</span>
        },
        {
            title: '작업',
            key: 'action',
            render: (_: any, record: Webcam) => (
                <Button size="small" icon={<EditOutlined />} onClick={() => showEditModal(record)}>
                    수정
                </Button>
            ),
        },
    ]

    return (
        <div>
            <div className="page-header">
                <Breadcrumb style={{ marginBottom: 8 }}>
                    <Breadcrumb.Item>관리자</Breadcrumb.Item>
                    <Breadcrumb.Item onClick={() => router.push('/ski-resorts')} className="cursor-pointer">
                        스키장 관리
                    </Breadcrumb.Item>
                    <Breadcrumb.Item onClick={() => router.push(`/ski-resorts/${id}`)} className="cursor-pointer">
                        {resortName}
                    </Breadcrumb.Item>
                    <Breadcrumb.Item>웹캠 관리</Breadcrumb.Item>
                </Breadcrumb>
                <Title level={2}>
                    <VideoCameraOutlined /> {resortName} 웹캠 관리
                </Title>
            </div>

            <Card className="content-card">
                <div style={{ marginBottom: 16 }}>
                    <Button icon={<ArrowLeftOutlined />} onClick={() => router.push(`/ski-resorts/${id}`)}>
                        스키장 상세로 돌아가기
                    </Button>
                </div>

                {loading ? (
                    <div style={{ textAlign: 'center', padding: 50 }}>
                        <Spin size="large" />
                    </div>
                ) : (
                    <Table
                        columns={columns}
                        dataSource={webcams}
                        rowKey="id"
                        pagination={false}
                    />
                )}
            </Card>

            <Modal
                title="웹캠 정보 수정"
                open={isModalVisible}
                onOk={handleUpdate}
                onCancel={() => {
                    setIsModalVisible(false)
                    form.resetFields()
                }}
                okText="저장"
                cancelText="취소"
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="name"
                        label="웹캠 이름"
                    >
                        <Input placeholder="웹캠 이름을 입력해주세요 (선택)" />
                    </Form.Item>
                    <Form.Item
                        name="description"
                        label="설명"
                    >
                        <Input.TextArea rows={2} placeholder="설명을 입력해주세요 (선택)" />
                    </Form.Item>
                    <Form.Item
                        name="url"
                        label="웹캠 URL"
                    >
                        <Input.TextArea rows={4} placeholder="웹캠 URL을 입력해주세요 (선택, m3u8 등)" />
                    </Form.Item>
                    <div style={{ marginTop: -8, marginBottom: 16, color: '#666', fontSize: '12px' }}>
                        * 모든 필드는 선택사항입니다. 변경하지 않을 필드는 비워두세요.<br />
                        * URL을 제거하려면 필드를 비워두고 저장하세요.
                    </div>
                </Form>
            </Modal>
        </div>
    )
}
